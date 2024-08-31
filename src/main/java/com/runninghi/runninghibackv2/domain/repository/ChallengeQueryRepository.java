package com.runninghi.runninghibackv2.domain.repository;

import com.querydsl.core.Tuple;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.ChallengeRankResponse;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeStatus;

import java.util.List;

public interface ChallengeQueryRepository {
    List<Challenge> findChallengesByStatusAndMember(ChallengeStatus status, Long memberNo);
    List<ChallengeRankResponse> findTop100ByChallengeNo(long challengeNo);
}
