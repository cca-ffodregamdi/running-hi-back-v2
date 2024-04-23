package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.score.GetRankingResponse;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByMember(Member member);

    @Query("SELECT s.scoreNo, s.distance, s.member.nickname," +
            "RANK() OVER (ORDER BY s.distance DESC) AS rank FROM Score s")
    List<GetRankingResponse> findAllRanking();

    @Query("SELECT s.scoreNo, s.distance, s.member.nickname, " +
            "(SELECT COUNT(*) + 1 FROM Score s2 WHERE s2.distance > s.distance)" +
            "FROM Score s WHERE s.member.memberNo = :memberNo")
    GetRankingResponse findMemberRanking(@Param("memberNo") Long memberNo);
}
