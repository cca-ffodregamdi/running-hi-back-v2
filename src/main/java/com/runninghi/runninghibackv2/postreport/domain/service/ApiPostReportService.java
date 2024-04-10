package com.runninghi.runninghibackv2.postreport.domain.service;

import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public interface ApiPostReportService {

    Member getMemberById(Long memberNo);
    Post getPostById(Long postNo);
    void deletePostById(Long postNo);
    void addReportedCountToMember(Long memberNo);
    void addReportedCountToPost(Long postNo);
    void resetReportedCountOfPost(Long postNo);
}
