package com.runninghi.runninghibackv2.infrastructure.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.repository.ChallengeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.runninghi.runninghibackv2.domain.entity.QChallenge.challenge;
import static com.runninghi.runninghibackv2.domain.entity.QMemberChallenge.memberChallenge;

@Repository
@RequiredArgsConstructor
public class ChallengeQueryRepositoryImpl implements ChallengeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Challenge> findChallengesByStatusAndMember(boolean status, Long memberNo) {

        return jpaQueryFactory.selectFrom(challenge)
                .where(challenge.status.eq(status)
                        .and(challenge.challengeNo.notIn(
                                JPAExpressions.select(memberChallenge.challenge.challengeNo)
                                        .from(memberChallenge)
                                        .where(memberChallenge.member.memberNo.eq(memberNo))
                        )))
                .fetch();
    }
}
