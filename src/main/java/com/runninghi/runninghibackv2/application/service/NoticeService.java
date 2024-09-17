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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateNoticeResponse createNotice(CreateNoticeRequest request, Long memberNo) {
        log.info("공지사항 생성 시도. 사용자 memberNo = {}", memberNo);
        Member member = findMemberByNo(memberNo);

        Notice notice = Notice.builder()
                .title(request.title())
                .content(request.content())
                .noticeWriter(member)
                .build();

        Notice savedNotice = noticeRepository.save(notice);
        log.info("공지사항 생성 완료. 공지사항 번호: {}", savedNotice.getNoticeNo());

        return CreateNoticeResponse.from(notice);
    }

    @Transactional
    public UpdateNoticeResponse updateNotice(Long noticeNo, UpdateNoticeRequest request) {
        log.info("공지사항 수정 시도. 공지사항 번호: {}", noticeNo);
        Notice notice = findNoticeById(noticeNo);

        notice.update(request.title(), request.content());
        log.info("공지사항 수정 완료. 공지사항 번호: {}", noticeNo);

        return UpdateNoticeResponse.from(notice);
    }

    @Transactional(readOnly = true)
    public GetNoticeResponse getNotice(Long noticeNo) {
        log.info("공지사항 조회 시도. 공지사항 번호: {}", noticeNo);
        Notice notice = findNoticeById(noticeNo);

        return GetNoticeResponse.from(notice);
    }

    @Transactional(readOnly = true)
    public NoticePageResponse<GetNoticeResponse> getAllNotices(Pageable pageable) {
        log.info("전체 공지사항 목록 조회 시도. 페이지: {}", pageable.getPageNumber());
        Page<Notice> noticePage = noticeRepository.findAll(pageable);

        Page<GetNoticeResponse> response = noticePage.map(GetNoticeResponse::from);
        log.info("전체 공지사항 목록 조회 완료.");

        return NoticePageResponse.from(response);
    }

    @Transactional
    public DeleteNoticeResponse deleteNotice(Long noticeNo) {
        log.info("공지사항 삭제 시도. 공지사항 번호: {}", noticeNo);
        Notice notice = findNoticeById(noticeNo);

        noticeRepository.delete(notice);
        log.info("공지사항 삭제 완료. 공지사항 번호: {}", noticeNo);

        return DeleteNoticeResponse.from(noticeNo);
    }

    private Member findMemberByNo(Long memberNo) {
        return memberRepository.findById(memberNo)
                .orElseThrow(() -> {
                    log.error("해당 번호의 회원을 찾을 수 없음. 회원 번호: {}", memberNo);
                    return new EntityNotFoundException();
                });
    }

    private Notice findNoticeById(Long noticeNo) {
        return noticeRepository.findById(noticeNo)
                .orElseThrow(() -> {
                    log.error("해당하는 번호의 공지사항을 찾을 수 없음. 공지사항 번호: {}", noticeNo);
                    return new EntityNotFoundException();
                });
    }
}
