package com.runninghi.runninghibackv2.member.domain.repository;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
}
