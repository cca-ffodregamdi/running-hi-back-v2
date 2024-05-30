package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.notice.request.CreateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.request.UpdateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.response.*;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Notice;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateNoticeResponse createNotice(CreateNoticeRequest request, Long memberNo) {
        Member member = memberRepository.findById(memberNo)
                .orElseThrow(EntityNotFoundException::new);

        Notice notice = Notice.builder()
                .title(request.title())
                .content(request.content())
                .noticeWriter(member)
                .build();

        noticeRepository.save(notice);

        return CreateNoticeResponse.from(notice);
    }

    @Transactional
    public UpdateNoticeResponse updateNotice(Long noticeNo, UpdateNoticeRequest request) {
        Notice notice = noticeRepository.findById(noticeNo)
                .orElseThrow(EntityNotFoundException::new);

        notice.update(request.title(), request.content());

        return UpdateNoticeResponse.from(notice);
    }

    @Transactional(readOnly = true)
    public GetNoticeResponse getNotice(Long noticeNo) {
        Notice notice = noticeRepository.findById(noticeNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetNoticeResponse.from(notice);
    }

    @Transactional(readOnly = true)
    public PageResponse<GetNoticeResponse> getAllNotices(Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.findAllBy(pageable);

        Page<GetNoticeResponse> response = noticePage.map(GetNoticeResponse::from);

        return PageResponse.from(response);
    }

    @Transactional
    public DeleteNoticeResponse deleteNotice(Long noticeNo) {
        Notice notice = noticeRepository.findById(noticeNo)
                .orElseThrow(EntityNotFoundException::new);

        noticeRepository.delete(notice);

        return DeleteNoticeResponse.from(noticeNo);
    }
}
