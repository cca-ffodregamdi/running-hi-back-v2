package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {
    Page<GetAllPostsResponse> findAllPostsByPageable(Pageable pageable);
    Page<GetAllPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo);
}
