package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.notice.request.CreateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.request.UpdateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.response.*;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Notice;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NoticeServiceTests {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long memberNo;

    private Notice notice1;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("Test Member")
                .build();
        memberRepository.save(member);
        memberNo = member.getMemberNo();

        notice1 = Notice.builder()
            .title("Notice 1")
            .content("Content 1")
            .noticeWriter(member)
            .build();
        Notice notice2 = Notice.builder()
                .title("Notice 2")
                .content("Content 2")
                .noticeWriter(member)
                .build();
        Notice notice3 = Notice.builder()
                .title("Notice 3")
                .content("Content 3")
                .noticeWriter(member)
                .build();

        noticeRepository.save(notice1);
        noticeRepository.save(notice2);
        noticeRepository.save(notice3);
    }

    @Test
    @DisplayName("공지사항 생성")
    void testCreateNotice() {
        CreateNoticeRequest request = new CreateNoticeRequest("Title", "Content");

        CreateNoticeResponse response = noticeService.createNotice(request, memberNo);

        assertNotNull(response);
        assertEquals(request.title(), response.title());
        assertEquals(request.content(), response.content());

        Optional<Notice> noticeOptional = noticeRepository.findById(response.noticeNo());
        assertTrue(noticeOptional.isPresent());
    }

    @Test
    @DisplayName("피드백 수정")
    void testUpdateNotice() {
        Notice notice = Notice.builder()
                .title("Old Title")
                .content("Old Content")
                .noticeWriter(memberRepository.findById(memberNo).orElseThrow())
                .build();
        noticeRepository.saveAndFlush(notice);

        UpdateNoticeRequest request = new UpdateNoticeRequest("New Title", "New Content");

        UpdateNoticeResponse response = noticeService.updateNotice(notice.getNoticeNo(), request);

        assertNotNull(response);
        assertEquals(request.title(), response.title());
        assertEquals(request.content(), response.content());

        Notice updatedNotice = noticeRepository.findById(notice.getNoticeNo()).orElseThrow();
        assertEquals(request.title(), updatedNotice.getTitle());
        assertEquals(request.content(), updatedNotice.getContent());
    }

    @Test
    @DisplayName("공지사항 조회")
    void testGetNotice() {
        Notice notice = Notice.builder()
                .title("Title")
                .content("Content")
                .noticeWriter(memberRepository.findById(memberNo).orElseThrow())
                .build();
        noticeRepository.save(notice);

        GetNoticeResponse response = noticeService.getNotice(notice.getNoticeNo());

        assertNotNull(response);
        assertEquals(notice.getTitle(), response.title());
        assertEquals(notice.getContent(), response.content());
    }

    @Test
    @DisplayName("공지사항 리스트 조회 ")
    void testGetAllNotices() {

        Pageable pageable = PageRequest.of(1, 2);
        PageResponse<GetNoticeResponse> response = noticeService.getAllNotices(pageable);

        assertNotNull(response);
        assertEquals(2, response.totalPages());
        assertEquals(1, response.content().size());
        assertEquals(2, response.currentPage());
        assertEquals(notice1.getTitle(), response.content().get(0).title());
        assertEquals(notice1.getContent(), response.content().get(0).content());
    }


    @Test
    @DisplayName("공지사항 삭제")
    void testDeleteNotice() {
        Notice notice = Notice.builder()
                .title("Title")
                .content("Content")
                .noticeWriter(memberRepository.findById(memberNo).orElseThrow())
                .build();
        noticeRepository.save(notice);

        DeleteNoticeResponse response = noticeService.deleteNotice(notice.getNoticeNo());

        assertNotNull(response);
        assertEquals(notice.getNoticeNo(), response.noticeNo());

        Optional<Notice> noticeOptional = noticeRepository.findById(notice.getNoticeNo());
        assertTrue(noticeOptional.isEmpty());
    }

    @Test
    @DisplayName("공지사항 수정 : EntityNotFound 존재하지않는 공지사항 번호")
    void testUpdateNonExistentNotice() {
        UpdateNoticeRequest request = new UpdateNoticeRequest("New Title", "New Content");
        assertThrows(EntityNotFoundException.class,
                () -> noticeService.updateNotice(999L, request));
    }

    @Test
    @DisplayName("공지사항 조회 : EntityNotFound 존재하지않는 공지사항 번호")
    void testDeleteNonExistentNotice() {
        assertThrows(EntityNotFoundException.class,
                () -> noticeService.deleteNotice(999L));
    }
}
