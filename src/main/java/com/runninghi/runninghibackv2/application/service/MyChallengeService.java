package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.memberchallenge.request.CreateMyChallengeRequest;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.CreateMyChallengeResponse;
import com.runninghi.runninghibackv2.application.dto.memberchallenge.response.GetMyChallengeResponse;
import com.runninghi.runninghibackv2.domain.entity.Challenge;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.MemberChallenge;
import com.runninghi.runninghibackv2.domain.repository.ChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberChallengeRepository;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateMyChallengeResponse createMyChallenge(CreateMyChallengeRequest request, Long memberNo) {

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

        return CreateMyChallengeResponse.from(memberChallenge);
    }

    @Transactional(readOnly = true)
    public List<GetMyChallengeResponse> getAllMyChallenges(Long memberNo) {

        Member member = memberRepository.findByMemberNo(memberNo);
        List<MemberChallenge> myChallenges = memberChallengeRepository.findByMember(member);

        return myChallenges.stream()
                .map(GetMyChallengeResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetMyChallengeResponse getMyChallengeById(Long memberNo, Long myChallengeNo) {

        MemberChallenge myChallenge = memberChallengeRepository.findById(myChallengeNo)
                .orElseThrow(EntityNotFoundException::new);

        isMyChallenge(myChallenge.getMember().getMemberNo(), memberNo);

        return GetMyChallengeResponse.from(myChallenge);
    }

    private void isMyChallenge(Long challengeMemberNo, Long memberNo) {
        if(challengeMemberNo != memberNo) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
