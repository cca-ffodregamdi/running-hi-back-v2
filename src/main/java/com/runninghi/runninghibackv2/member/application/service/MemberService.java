package com.runninghi.runninghibackv2.member.application.service;

import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.member.domain.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private static final String INVALID_MEMBER_ID_MESSAGE = "Invalid member Id";

    public Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER_ID_MESSAGE));
    }

    @Transactional
    public void addReportedCount(Long memberNo) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(() -> new EntityNotFoundException(INVALID_MEMBER_ID_MESSAGE));

        member.addReportedCount();
    }
}
