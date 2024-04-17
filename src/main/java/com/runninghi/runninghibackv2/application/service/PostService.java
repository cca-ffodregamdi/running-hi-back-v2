package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.post.request.CreateRecordRequest;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.entity.Keyword;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.PostKeyword;
import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.request.CreatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.application.dto.post.response.CreatePostResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.UpdatePostResponse;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.vo.GpxDataVO;
import com.runninghi.runninghibackv2.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.domain.service.CalculateGPX;
import com.runninghi.runninghibackv2.domain.service.PostChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.Resource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CalculateGPX calculateGPX;
    private final PostChecker postChecker;
    private final PostRepository postRepository;
    private final PostKeywordService postKeywordService;
    private final UpdatePostService updateService;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<GetAllPostsResponse> getPostScroll(Pageable pageable) {

        Page<Post> posts = postRepository.findAllByOrderByCreateDateDesc(pageable);

        return posts.map(GetAllPostsResponse::from);

        //키워드 필터링

    }

    @Transactional
    public CreatePostResponse createRecordAndPost(CreatePostRequest request, Resource gpxFile) throws ParserConfigurationException, IOException, SAXException {

        postChecker.checkPostValidation(request.postTitle(), request.postContent());

        GpxDataVO gpxDataVO = calculateGPX.getDataFromGpxFile(gpxFile);

        Member member = memberRepository.findByMemberNo(request.memberNo());

        Post createdPost = postRepository.save(Post.builder()
                .member(member)
                .role(member.getRole())
                .postTitle(request.postTitle())
                .postContent(request.postContent())
                .locationName(request.locationName())
                .gpxDataVO(gpxDataVO)
                .status(true)
                .build());

        postKeywordService.createPostKeyword(createdPost, request.keywordList());

        GpxDataVO postGpxVO = createdPost.getGpxDataVO();

        return new CreatePostResponse(createdPost.getPostNo(), postGpxVO.getDistance(), postGpxVO.getTime(),
                postGpxVO.getKcal(), postGpxVO.getSpeed(), postGpxVO.getMeanPace());
    }

    @Transactional
    public CreatePostResponse createRecord(CreateRecordRequest request, Resource gpxFile) throws ParserConfigurationException, IOException, SAXException {

        GpxDataVO gpxDataVO = calculateGPX.getDataFromGpxFile(gpxFile);

        Member member = memberRepository.findByMemberNo(request.memberNo());

        Post createdPost = postRepository.save(Post.builder()
                .member(member)
                .role(member.getRole())
                .locationName(request.locationName())
                .gpxDataVO(gpxDataVO)
                .status(false)
                .build());

        GpxDataVO postGpxVO = createdPost.getGpxDataVO();



        return new CreatePostResponse(createdPost.getPostNo(), postGpxVO.getDistance(), postGpxVO.getTime(),
                postGpxVO.getKcal(), postGpxVO.getSpeed(), postGpxVO.getMeanPace());
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
    public void deletePost(Long memberNo, Long postNo) {

        Post post = findPostByNo(postNo);

        postChecker.isWriter(memberNo, post.getMember().getMemberNo());

        postKeywordService.deletePostKeyword(postNo);
        postRepository.deleteById(postNo);
    }


    @Transactional
    public void deleteReportedPost(Long postNo) {
    // 관리자용 신고 게시글 삭제 메소드
        postKeywordService.deletePostKeyword(postNo);
        postRepository.deleteById(postNo);
        
    }

    @Transactional(readOnly = true)
    public GetPostResponse getPost(Long postNo) {

        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        List<PostKeyword> list = postKeywordService.getKeywordsByPost(post);

        List<Keyword> keywordList = new ArrayList<>();

        for (PostKeyword postKeyword : list) {
            keywordList.add(postKeyword.getKeyword());
        }

        return GetPostResponse.from(post, keywordList);
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

    private Post findPostByNo(Long postNo) {
        return postRepository.findById(postNo)
                .orElseThrow(EntityNotFoundException::new);
    }
}
