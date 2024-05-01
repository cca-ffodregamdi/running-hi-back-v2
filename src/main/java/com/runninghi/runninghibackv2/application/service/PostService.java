package com.runninghi.runninghibackv2.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.application.dto.post.request.CreatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.response.*;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.Score;
import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.repository.ScoreRepository;
import com.runninghi.runninghibackv2.domain.service.GpxCalculator;
import com.runninghi.runninghibackv2.domain.service.GpxCoordinateExtractor;
import com.runninghi.runninghibackv2.domain.service.PostChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static com.runninghi.runninghibackv2.domain.entity.QKeyword.keyword;
import static com.runninghi.runninghibackv2.domain.entity.QPost.post;
import static com.runninghi.runninghibackv2.domain.entity.QPostKeyword.postKeyword;

@Service
@RequiredArgsConstructor
public class PostService {

    private final GpxCalculator calculateGPX;
    private final PostChecker postChecker;
    private final PostRepository postRepository;
    private final PostKeywordService postKeywordService;
    private final UpdatePostService updateService;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final GpxCoordinateExtractor gpxCoordinateExtractor;
    private final ScoreRepository scoreRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    private String buildKey(String dirName) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        String newFileName = UUID.randomUUID() + "_" + now;

        return dirName + "/" + newFileName + ".txt";
    }

    private String uploadGpxToS3(String gpxData, String dirName) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(gpxData.getBytes());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();

            String key = buildKey(dirName);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("text/plain");
            metadata.setContentLength(outputStream.size());
            amazonS3Client.putObject(bucketName, key, new ByteArrayInputStream(outputStream.toByteArray()), metadata);

            return amazonS3Client.getUrl(bucketName, key).toString();
        }
    }

    @Transactional(readOnly = true)
    public Page<GetAllPostsResponse> getPostScroll(Pageable pageable, List<String> keywordList) {

        List<Long> keywordNos = jpaQueryFactory.select(keyword.keywordNo)
                .from(keyword)
                .where(keyword.keywordName.in(keywordList))
                .fetch();

        List<Post> posts = jpaQueryFactory.select(post)
                .leftJoin(postKeyword)
                .where(postKeyword.keyword.keywordNo.in(keywordNos))
                .fetch();

        return new PageImpl<>(posts.stream().map(GetAllPostsResponse::from).toList(), pageable, posts.size());
    }

    @Transactional
    public CreateRecordResponse createRecord(Long memberNo, String gpxFile) throws Exception {

        byte[] compressedData = Base64.getDecoder().decode(gpxFile);
        String gpxData = decompress(compressedData);

        //GPX 저장
        GpxDataVO gpxDataVO = calculateGPX.getDataFromGpxFile(gpxData);

        Member member = memberRepository.findByMemberNo(memberNo);

        String gpxUrl = uploadGpxToS3(gpxData, member.getMemberNo().toString());

        Post createdPost = postRepository.save(Post.builder()
                .member(member)
                .role(member.getRole())
                .gpxDataVO(gpxDataVO)
                .gpxUrl(gpxUrl)
                .status(false)
                .build());

        createOrUpdateScore(member, gpxDataVO);
        GpxDataVO postGpxVO = createdPost.getGpxDataVO();

        return new CreateRecordResponse(createdPost.getPostNo(), postGpxVO.getDistance(), postGpxVO.getTime(),
                postGpxVO.getKcal(), postGpxVO.getSpeed(), postGpxVO.getMeanPace());
    }


    private String decompress(byte[] value) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        GZIPInputStream gzipInStream = new GZIPInputStream(new ByteArrayInputStream(value));

        int size = 0;
        byte[] buffer = new byte[1024];
        while ( (size = gzipInStream.read(buffer)) > 0 ) {
            outStream.write(buffer, 0, size);
        }
        outStream.flush();
        outStream.close();

        return new String(outStream.toByteArray());
    }

    @Transactional
    public CreatePostResponse createPost(Long memberNo, CreatePostRequest request) {

        postChecker.checkPostValidation(request.postTitle(), request.postContent());

        Post post = postRepository.findById(request.postNo())
                        .orElseThrow(EntityNotFoundException::new);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        postKeywordService.createPostKeyword(post, request.keywordList());
        savePostImages(request.imageUrlList(), post.getPostNo());

        post.shareToPost(request);

        return new CreatePostResponse(post.getPostNo());

    }


    @Transactional
    public UpdatePostResponse updatePost(Long memberNo, Long postNo, UpdatePostRequest request) {

        Post post = findPostByNo(postNo);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        post.update(request);

        updateService.updatePostKeyword(post, request.keywordList());

        return UpdatePostResponse.from(post);
    }

    @Transactional
    public DeletePostResponse deletePost(Long memberNo, Long postNo) {

        Post post = findPostByNo(postNo);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        postKeywordService.deletePostKeyword(postNo);
        postRepository.deleteById(postNo);

        return DeletePostResponse.from(postNo);
    }


    @Transactional
    public void deleteReportedPost(Long postNo) {
        // 관리자용 신고 게시글 삭제 메소드
        postKeywordService.deletePostKeyword(postNo);
        postRepository.deleteById(postNo);

    }

//    @Transactional(readOnly = true)
//    public GetPostResponse getPostByPostNo(Long postNo) {
//
//        Post post = postRepository.findById(postNo)
//                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
//
//        List<PostKeyword> list = postKeywordService.getKeywordsByPost(post);
//
//        List<Keyword> keywordList = new ArrayList<>();
//
//        for (PostKeyword postKeyword : list) {
//            keywordList.add(postKeyword.getKeyword());
//        }
//
//        return GetPostResponse.from(post, keywordList);
//    }

    private Post findPostByNo(Long postNo) {
        return postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Page<GetAllPostsResponse> getReportedPostScroll(Pageable pageable) {

        Page<Post> posts = postRepository.findAllByReportCntIsGreaterThan(0, pageable);

        return posts.map(GetAllPostsResponse::from);
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

    private void savePostImages(List<String> imageUrlList, Long postNo) {
        imageService.savePostNo(imageUrlList, postNo);
    }


    public GpxDataResponse getGpxLonLatData(Long postNo) throws ParserConfigurationException, IOException, SAXException {
        Post post = findPostByNo(postNo);
        String gpxUrl = post.getGpxUrl();

        URL url = new URL(gpxUrl);

        try (InputStream inputStream = url.openStream()) {
            return new GpxDataResponse(gpxCoordinateExtractor.extractCoordinates(inputStream));
        }
    }

    public void createOrUpdateScore(Member member, GpxDataVO gpxDataVO) {
        Optional<Score> score = scoreRepository.findByMember(member);

        if (scoreRepository.findByMember(member).isEmpty()) {
            scoreRepository.save(Score.builder()
                    .distance(gpxDataVO.getDistance())
                    .member(member)
                    .build());
            return;
        }
        score.get().addDistance(gpxDataVO.getDistance());
    }
}
