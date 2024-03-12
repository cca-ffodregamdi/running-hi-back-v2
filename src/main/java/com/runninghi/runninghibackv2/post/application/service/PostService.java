package com.runninghi.runninghibackv2.post.application.service;

import com.runninghi.runninghibackv2.post.application.dto.request.CreatePostRequest;
import com.runninghi.runninghibackv2.post.application.dto.response.CreatePostResponse;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;
import com.runninghi.runninghibackv2.post.domain.aggregate.vo.GpxDataVO;
import com.runninghi.runninghibackv2.post.domain.repository.PostRepository;
import com.runninghi.runninghibackv2.post.domain.service.CalculateGPX;
import com.runninghi.runninghibackv2.post.domain.service.PostChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CalculateGPX calculateGPX;
    private final PostChecker postChecker;
    private final PostRepository postRepository;

    @Transactional
    public CreatePostResponse createRecordAndPost(CreatePostRequest request, MultipartFile gpxFile) {

        postChecker.checkPostValidation(request.postTitle(), request.postContent());

        GpxDataVO gpxDataVO = calculateGPX.getDataFromGpxFile(gpxFile);

        Post createdPost = postRepository.save(new Post.Builder()
                .member(request.member())
                .role(request.role())
                .postTitle(request.postTitle())
                .postContent(request.postContent())
                .locationName(request.locationName())
                .gpxDataVO(gpxDataVO)
                .build());

        GpxDataVO postGpxVO = createdPost.getGpxDataVO();

        return new CreatePostResponse(postGpxVO.getDistance(), postGpxVO.getTime(),
                postGpxVO.getKcal(), postGpxVO.getSpeed(), postGpxVO.getMeanPace());
    }



}
