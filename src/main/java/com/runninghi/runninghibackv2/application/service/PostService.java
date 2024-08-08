package com.runninghi.runninghibackv2.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.request.CreatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.response.*;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import com.runninghi.runninghibackv2.domain.entity.*;
import com.runninghi.runninghibackv2.domain.entity.vo.GpsDataVO;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeCategory;
import com.runninghi.runninghibackv2.domain.enumtype.Difficulty;
import com.runninghi.runninghibackv2.domain.repository.*;
import com.runninghi.runninghibackv2.domain.service.GpsCoordinateExtractor;
import com.runninghi.runninghibackv2.domain.service.PostChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.locationtech.jts.geom.Point;

import static com.runninghi.runninghibackv2.domain.entity.QImage.image;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostChecker postChecker;
    private final PostRepository postRepository;
    private final PostKeywordService postKeywordService;
    private final UpdatePostService updateService;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final GpsCoordinateExtractor gpsCoordinateExtractor;
    private final PostQueryRepository postQueryRepository;
    private final MemberChallengeRepository memberChallengeRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private String buildKey(String dirName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        String newFileName = UUID.randomUUID() + "_" + now;

        return dirName + "/" + newFileName + ".txt";
    }

    public String uploadGpsToS3(MultipartFile file, String dirName) throws IOException {
        String key = buildKey(dirName);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("text/plain");
        objectMetadata.setContentLength(file.getSize());

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())) {
            amazonS3Client.putObject(bucketName, key, byteArrayInputStream, objectMetadata);
            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (IOException e) {
            logger.error("GPS TXT 파일 S3 업로드 에러: ", e);
            throw e;
        }
    }

    private String getMainData(int dataNo, GpsDataVO gpsDataVO) {

        String mainData = null;

        switch (dataNo) {
            case 0:
                mainData = gpsDataVO.getDistance() + "km";
                break;
            case 1:
                mainData = gpsDataVO.getTime()/60 + "분 " + gpsDataVO.getTime()%60 + "초";
                break;
            case 2:
                mainData = gpsDataVO.getKcal() + "Kcal";
                break;
            case 3:
                mainData = gpsDataVO.getMeanPace()/60 + "' " + gpsDataVO.getMeanPace()%60 + "\"";
                break;
        }
        return  mainData;
    }

    private Post findPostByNo(Long postNo) {
        return postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void savePostImages(List<String> imageUrlList, Long postNo) {
        imageService.savePostNo(imageUrlList, postNo);
    }

    private void savePostImage(String imageUrl, Long postNo) {
        imageService.savePostNo(imageUrl, postNo);
    }

    private String createPostTitle(GpsDataVO request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M월 d일", Locale.KOREAN);
        return request.getRunStartTime().format(formatter) + " " + request.getLocationName() + " 러닝";
    }

    private String readFileContent(MultipartFile file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
    private List<Integer> getIntegerList(JsonNode node) {
        return StreamSupport.stream(node.spliterator(), false)
                .map(JsonNode::asInt)
                .collect(Collectors.toList());
    }

    public GpsDataVO getRunDataFromTxt(MultipartFile file) throws IOException {
        String content = readFileContent(file);
        JsonNode rootNode = objectMapper.readTree(content);

        JsonNode runInfoNode = rootNode.get("runInfo");
        JsonNode sectionDataNode = rootNode.get("sectionData");
        JsonNode gpsDataNode = rootNode.get("gpsData");

        Point startPoint = null;
        if (gpsDataNode.isArray() && gpsDataNode.size() > 0) {
            JsonNode firstGpsPoint = gpsDataNode.get(0);
            double lon = firstGpsPoint.get("lon").asDouble();
            double lat = firstGpsPoint.get("lat").asDouble();
            startPoint = geometryFactory.createPoint(new Coordinate(lon, lat));
        }

        return GpsDataVO.builder()
                .locationName(runInfoNode.get("location").asText())
                .startPoint(startPoint)
                .runStartTime(LocalDateTime.parse(runInfoNode.get("runStartDate").asText()))
                .distance((float) runInfoNode.get("distance").asDouble())
                .time(runInfoNode.get("time").asInt())
                .kcal(runInfoNode.get("kcal").asInt())
                .meanPace(runInfoNode.get("meanPace").asInt())
                .sectionPace(getIntegerList(sectionDataNode.get("pace")))
                .sectionKcal(getIntegerList(sectionDataNode.get("kcal")))
                .difficulty(Difficulty.valueOf(runInfoNode.get("difficulty").asText()))
                .build();
    }

    @Transactional(readOnly = true)
    public PageResultData<GetAllPostsResponse>  getPostScroll(Long memberNo, Pageable pageable, String sort, int distance) {
        if (sort.equalsIgnoreCase("latest")) {
            return postQueryRepository.findAllPostsByLatest(memberNo, pageable, distance);
        } else if (sort.equalsIgnoreCase("recommended")) {
            return postQueryRepository.findAllPostsByRecommended(memberNo, pageable, distance);
        } else if (sort.equalsIgnoreCase("like")){
            return postQueryRepository.findAllPostsByLikeCnt(memberNo, pageable, distance);
        } else if (sort.equalsIgnoreCase("distance")){
            return postQueryRepository.findAllPostsByDistance(memberNo, pageable, distance);
        } else {
            return postQueryRepository.findAllPostsByLatest(memberNo, pageable, distance);
        }
    }

    @Transactional(readOnly = true)
    public GetPostResponse getPostDetailByPostNo(Long memberNo, Long postNo) {
        return postQueryRepository.getPostDetailByPostNo(memberNo, postNo);
    }

    @Transactional(readOnly = true)
    public PageResultData<GetMyPostsResponse> getMyPostsScroll(Pageable pageable, Long memberNo) {
        return  postQueryRepository.findMyPostsByPageable(pageable, memberNo);
    }

    @Transactional(readOnly = true)
    public PageResultData<GetAllPostsResponse> getMyLikedPosts(Pageable pageable, Long memberNo) {
        return  postQueryRepository.findMyLikedPostsByPageable(pageable, memberNo);
    }
    @Transactional(readOnly = true)
    public PageResultData<GetAllPostsResponse> getMyBookmarkedPosts(Pageable pageable, Long memberNo) {
        return  postQueryRepository.findMyBookmarkedPostsByPageable(pageable, memberNo);
    }

    @Transactional
    public CreateRecordResponse createRecord(Long memberNo, MultipartFile file) throws IOException {
        GpsDataVO gpsDataVO = getRunDataFromTxt(file);

        Member member = memberRepository.findByMemberNo(memberNo);

        String gpsUrl = uploadGpsToS3(file, member.getMemberNo().toString());

        updateRecordOfMyChallenges(member, gpsDataVO);

        Post createdPost = postRepository.save(Post.builder()
                .member(member)
                .role(member.getRole())
                .gpsDataVO(gpsDataVO)
                .gpxUrl(gpsUrl)
                .status(false)
                .postTitle(createPostTitle(gpsDataVO))
                .build());

        return new CreateRecordResponse(createdPost.getPostNo());
    }


    @Transactional
    public CreatePostResponse createPost(Long memberNo, CreatePostRequest request) {
        postChecker.checkPostValidation(request.postContent());

        Post post = postRepository.findById(request.postNo())
                        .orElseThrow(EntityNotFoundException::new);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        savePostImage(request.imageUrl(), post.getPostNo());

        String mainData = getMainData(request.mainData(), post.getGpsDataVO());

        post.shareToPost(request, mainData);

        return new CreatePostResponse(post.getPostNo());

    }

    @Transactional
    public UpdatePostResponse updatePost(Long memberNo, Long postNo, UpdatePostRequest request) {
        Post post = findPostByNo(postNo);
        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        String mainData = getMainData(request.mainData(), post.getGpsDataVO());

        post.update(request, mainData);
        imageService.updateImage(postNo, request.imageUrl());

        return UpdatePostResponse.from(post, request.imageUrl());
    }

    @Transactional
    public DeletePostResponse deletePost(Long memberNo, Long postNo) {

        Post post = findPostByNo(postNo);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        postKeywordService.deletePostKeyword(postNo);
        postRepository.deleteById(postNo);

        return DeletePostResponse.from(postNo);
    }


    // 수정 필요!
    @Transactional(readOnly = true)
    public Page<GetReportedPostsResponse> getReportedPostScroll(Pageable pageable) {

        Page<Post> posts = postRepository.findAllByReportCntIsGreaterThan(0, pageable);

        List<Long> postNos = posts.getContent().stream().map(Post::getPostNo).collect(Collectors.toList());

        Image mainImage = jpaQueryFactory.select(image)
                .from(image)
                .where(image.postNo.in(postNos))
                .limit(1)
                .fetchOne();

        List<GetReportedPostsResponse> responses = posts.stream()
                .map(post -> GetReportedPostsResponse.from(post, mainImage.getImageUrl()))
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, posts.getTotalElements());
    }

    @Transactional
    public void addReportedCount(Long postNo) {
        Post post = findPostByNo(postNo);
        post.addReportedCount();
    }

    @Transactional
    public void resetReportedCount(Long postNo) {

        Post post = findPostByNo(postNo);

        post.resetReportedCount();
    }

    private void updateRecordOfMyChallenges(Member member, GpsDataVO gpsDataVO) {
        List<MemberChallenge> myChallenges = memberChallengeRepository.findByMember(member);

        if (myChallenges.isEmpty()) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(LocalTime.MAX);

        for (MemberChallenge myChallenge : myChallenges) {
            if (myChallenge.getChallenge().getChallengeCategory() == ChallengeCategory.ATTENDANCE) {
                Optional<Post> post = postRepository.findFirstByMemberAndCreateDateBetween(member, startOfDay, endOfDay);
                if (post.isEmpty()) {
                    myChallenge.updateRecord();
                }
                continue;
            }
            myChallenge.updateRecord(gpsDataVO);
        }
    }

}
