package com.runninghi.runninghibackv2.postreport.domain.service;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public interface ApiPostReportService {

    Member getMemberById(Long memberNo);
    Post getPostById(Long postNo);
}
