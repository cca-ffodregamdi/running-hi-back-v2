package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberChallengeRepository extends JpaRepository<MemberChallenge, Long> {
    List<MemberChallenge> findByMember(Member member);
}
