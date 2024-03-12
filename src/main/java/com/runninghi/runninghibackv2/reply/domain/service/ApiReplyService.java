package com.runninghi.runninghibackv2.reply.domain.service;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.post.domain.aggregate.entity.Post;

public interface ApiReplyService {

    Member getMemberByMemberNo(Long memberNo);

    Post getPostByPostNo(Long postNo);

}
