package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByMemberNo(Long memberNo);

    Optional<Member> findByKakaoId(String kakaoId);

    @Query("SELECT m FROM Member m WHERE m.deactivateDate <= :deactivateDate")
    List<Member> findAllByDeactivateDate(LocalDateTime deactivateDate);

}
