package com.runninghi.runninghibackv2.post.application.service;

import com.runninghi.runninghibackv2.keyword.domain.aggregate.entity.Keyword;
import com.runninghi.runninghibackv2.post.postkeyword.application.service.PostKeywordService;
import com.runninghi.runninghibackv2.post.postkeyword.domain.aggregate.entity.PostKeyword;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.request.UpdatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.GetPostResponse;
import com.runninghi.runninghibackv2.post.application.dto.response.UpdatePostResponse;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.aggregate.vo.GpxDataVO;
import com.runninghi.runninghibackv2.post.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.post.domain.service.ApiPostService;
import com.runninghi.runninghibackv2.post.domain.service.CalculateGPX;
import com.runninghi.runninghibackv2.post.domain.service.PostChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CalculateGPX calculateGPX;
    private final PostChecker postChecker;
    private final PostRepository postRepository;
    private final PostKeywordService postKeywordService;
    private ApiPostService apiPostService;

    @Transactional
    public CreatePostResponse createRecordAndPost(CreatePostRequest request) {

        postChecker.checkPostValidation(request.postTitle(), request.postContent());

        GpxDataVO gpxDataVO = calculateGPX.getDataFromGpxFile();

//        Member member = apiPostService.getMemberById(request.memberNo());

        Post createdPost = postRepository.save(Post.builder()
//                .member(member)
//                .role(member.getRole())
                .postTitle(request.postTitle())
                .postContent(request.postContent())
                .locationName(request.locationName())
                .gpxDataVO(gpxDataVO)
                .build());

        postKeywordService.createPostKeyword(createdPost, request.keywordList());

        GpxDataVO postGpxVO = createdPost.getGpxDataVO();

        return new CreatePostResponse(createdPost.getPostNo(), postGpxVO.getDistance(), postGpxVO.getTime(),
                postGpxVO.getKcal(), postGpxVO.getSpeed(), postGpxVO.getMeanPace());
    }

    @Transactional
    public UpdatePostResponse updatePost(Long postNo, UpdatePostRequest request) {

        Post post = postRepository.findById(postNo)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));

        post.update(request);

        postKeywordService.updatePostKeyword(post, request.keywordList());

        return UpdatePostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postNo) {

        postRepository.deleteById(postNo);
        postKeywordService.deletePostKeyword(postNo);

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



}
