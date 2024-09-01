package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.*;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.request.CreateMemberChallengeRequest;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.enumtype.ChallengeStatus;
import com.runninghi.runninghibackv2.domain.repository.ChallengeQueryRepository;
import com.runninghi.runninghibackv2.domain.repository.ChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberRepository memberRepository;
    private final ChallengeQueryRepository challengeQueryRepository;

    @Transactional
    public CreateMemberChallengeResponse createMemberChallenge(CreateMemberChallengeRequest request, Long memberNo) {

        Challenge challenge = challengeRepository.findById(request.challengeNo())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByMemberNo(memberNo);

        if(memberChallengeRepository.findByMemberAndChallenge(member, challenge).isPresent()) {
            throw new IllegalArgumentException("이미 참여중인 챌린지입니다.");
        }

        MemberChallenge memberChallenge = MemberChallenge.builder()
                .challenge(challenge)
                .member(member)
                .build();

        memberChallengeRepository.save(memberChallenge);

        return CreateMemberChallengeResponse.from(memberChallenge);
    }

    @Transactional(readOnly = true)
    public GetAllMemberChallengeResponse getMemberChallengesByStatus(Long memberNo, ChallengeStatus status) {

        List<MemberChallengeListResponse> memberChallengeList =
                challengeQueryRepository.findMemberChallengesByStatus(memberNo, status).stream()
                .map(MemberChallengeListResponse::from)
                .toList();
        int memberChallengeCount = memberChallengeList.size();

        return new GetAllMemberChallengeResponse(memberChallengeList, memberChallengeCount);
    }

    @Transactional(readOnly = true)
    public GetMemberChallengeResponse getMemberChallengeByChallengeId(Long memberNo, Long challengeNo) {

        Member member = memberRepository.findByMemberNo(memberNo);
        Challenge challenge = challengeRepository.findById(challengeNo)
                .orElseThrow(EntityNotFoundException::new);

        MemberChallenge memberChallenge = memberChallengeRepository.findByMemberAndChallenge(member, challenge)
                .orElseThrow(EntityNotFoundException::new);

        List<ChallengeRankResponse> challengeRanking = challengeQueryRepository.findTop100Ranking(challengeNo);
        ChallengeRankResponse memberRanking = challengeQueryRepository.findMemberRanking(challengeNo, memberNo);

        return GetMemberChallengeResponse.from(memberChallenge, challengeRanking, memberRanking);
    }
}
