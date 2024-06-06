package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.post.response.GetAllPostsResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostQueryRepository {
    PageResultData<GetAllPostsResponse> findAllPostsByPageable(Pageable pageable);
    PageResultData<GetAllPostsResponse> findMyPostsByPageable(Pageable pageable, Long memberNo);
}
