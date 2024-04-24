package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Post;
import com.runninghi.runninghibackv2.domain.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByPost_PostNo (Long postNo);

    List<Reply> findAllByWriter_MemberNo(Long memberNo);

    void deleteAllByPost(Post post);

    void deleteAllByWriter(Member member);

}
