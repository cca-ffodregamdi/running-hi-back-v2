package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.notice.request.CreateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.request.UpdateNoticeRequest;
import com.runninghi.runninghibackv2.application.dto.notice.response.*;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.Notice;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.NoticeRepository;
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

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .name("Test Member")
                .build();
        memberRepository.save(member);
        memberNo = member.getMemberNo();
    }

    @Test
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
    void testGetAllNotices() {
        Notice notice = Notice.builder()
                .title("Title")
                .content("Content")
                .noticeWriter(memberRepository.findById(memberNo).orElseThrow())
                .build();
        noticeRepository.save(notice);

        Pageable pageable = PageRequest.of(0, 10);
        PageResponse<GetNoticeResponse> response = noticeService.getAllNotices(pageable);

        assertNotNull(response);
        assertEquals(1, response.totalPages());
        assertEquals(1, response.content().size());
        assertEquals(notice.getTitle(), response.content().get(0).title());
        assertEquals(notice.getContent(), response.content().get(0).content());
    }

    @Test
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
}
