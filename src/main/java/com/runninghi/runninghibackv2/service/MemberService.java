package com.runninghi.runninghibackv2.service;

import com.runninghi.runninghibackv2.dto.request.UpdateMemberInfoRequest;
import com.runninghi.runninghibackv2.dto.response.GetMemberResponse;
import com.runninghi.runninghibackv2.dto.response.UpdateMemberInfoResponse;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.repository.MemberRepository;
import com.runninghi.runninghibackv2.utils.MemberChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberChecker memberChecker;

    public Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public UpdateMemberInfoResponse updateMemberInfo(Long memberNo, UpdateMemberInfoRequest request) throws BadRequestException {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        memberChecker.checkNicknameValidation(request.nickname());

        member.updateNickname(request.nickname());
        Member updatedMember = memberRepository.save(member);

        return UpdateMemberInfoResponse.from(updatedMember.getNickname());
    }

    @Transactional(readOnly = true)
    public GetMemberResponse getMemberInfo(Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetMemberResponse.from(member);
    }

    public void addReportedCount(Long memberNo) {

        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        member.addReportedCount();
    }
}
