package com.runninghi.runninghibackv2.repository;

import com.runninghi.runninghibackv2.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberNo(Long memberNo);

    Optional<Member> findByKakaoId(String kakaoId);

}
