package com.runninghi.runninghibackv2.reply.domain.repository;

import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<List<Reply>> findAllByPost_PostNo (Long postNo);

}
