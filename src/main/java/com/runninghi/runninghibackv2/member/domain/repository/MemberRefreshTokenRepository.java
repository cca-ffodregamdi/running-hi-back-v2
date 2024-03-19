package com.runninghi.runninghibackv2.member.domain.repository;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<Member, Long> {
}
