package com.runninghi.runninghibackv2.bookmark.domain.service;

import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public interface ApiBookmarkService {
    Member getMemberById(Long memberNo);

    Post getPostById(Long postNo);
}
