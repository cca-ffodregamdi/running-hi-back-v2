package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetMyPostsResponse;
import com.runninghi.runninghibackv2.application.dto.post.response.GetPostResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {
    PageResultData<GetMyPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo);
    GetPostResponse getPostDetailByPostNo(Long memberNo, Long postNo);
    PageResultData<GetAllPostsResponse> findAllPostsByLatest(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findAllPostsByRecommended(Long memberNo, Pageable pageable, int distance);
    PageResultData<GetAllPostsResponse> findMyLikedPostsByPageable(Pageable pageable, Long memberNo);
    PageResultData<GetAllPostsResponse> findMyBookmarkedPostsByPageable(Pageable pageable, Long memberNo);
}
