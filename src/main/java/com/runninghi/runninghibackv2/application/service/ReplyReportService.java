package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.postreport.response.GetAllPostReportsResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.request.CreateReplyReportRequest;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.CreateReplyReportResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.DeleteReplyReportResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.GetReplyReportResponse;
import com.runninghi.runninghibackv2.application.dto.replyreport.response.HandleReplyReportResponse;
import com.runninghi.runninghibackv2.common.response.PageResult;
import com.runninghi.runninghibackv2.domain.entity.Member;
import com.runninghi.runninghibackv2.domain.entity.PostReport;
import com.runninghi.runninghibackv2.domain.entity.Reply;
import com.runninghi.runninghibackv2.domain.entity.ReplyReport;
import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.domain.repository.MemberRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyReportRepository;
import com.runninghi.runninghibackv2.domain.repository.ReplyRepository;
import com.runninghi.runninghibackv2.domain.service.ReplyReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyReportService {

    private final ReplyReportRepository replyReportRepository;
    private final MemberRepository memberRepository;
    private final ReplyRepository replyRepository;
    private final ReplyReportChecker replyReportChecker;

    // 댓글 신고 저장
    @Transactional
    public CreateReplyReportResponse createReplyReport(Long memberNo, CreateReplyReportRequest request) {

        replyReportChecker.checkReplyReportValidation(request);

        Member reporter = memberRepository.findByMemberNo(memberNo);
        Reply reportedReply = replyRepository.findById(request.reportedReplyNo())
                .orElseThrow(EntityNotFoundException::new);

        ReplyReport replyReport = ReplyReport.builder()
                .category(request.category())
                .content(request.content())
                .status(ProcessingStatus.INPROGRESS)
                .reporter(reporter)
                .reportedReply(reportedReply)
                .replyContent(reportedReply.getReplyContent())
                .build();

        replyReportRepository.save(replyReport);
        reportedReply.addReportedCount();

        return CreateReplyReportResponse.from(replyReport);
    }

    // 댓글 신고 전체 조회
    @Transactional(readOnly = true)
    public Page<GetReplyReportResponse> getReplyReports(Pageable pageable) {

        Page<ReplyReport> replyReports = replyReportRepository.findAll(pageable);

        List<GetReplyReportResponse> responses = replyReports.stream()
                .map(GetReplyReportResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(responses, pageable, replyReports.getTotalElements());
    }

    // 댓글 신고 상세 조회
    @Transactional(readOnly = true)
    public GetReplyReportResponse getReplyReportById(Long replyReportNo) {

        ReplyReport replyReport = replyReportRepository.findById(replyReportNo)
                .orElseThrow(EntityNotFoundException::new);

        return GetReplyReportResponse.from(replyReport);
    }

    // 신고된 댓글의 모든 신고 내역 조회
    @Transactional(readOnly = true)
    public List<GetReplyReportResponse> getReplyReportScrollByReplyId(Long replyNo) {

        Reply reportedReply = replyRepository.findById(replyNo)
                .orElseThrow(EntityNotFoundException::new);

        List<ReplyReport> replyReports = replyReportRepository.findAllByReportedReply(reportedReply);

        return replyReports.stream()
                .map(GetReplyReportResponse::from)
                .collect(Collectors.toList());
    }

    // 댓글 신고 수락/거절 처리
    @Transactional
    public List<HandleReplyReportResponse> handleReplyReports(boolean isAccepted, Long replyNo) {

        Reply reportedReply = replyRepository.findById(replyNo)
                .orElseThrow(EntityNotFoundException::new);

        List<ReplyReport> replyReportList = replyReportRepository.findAllByReportedReply(reportedReply);
        ProcessingStatus status;

        if(isAccepted) {
            status = ProcessingStatus.ACCEPTED;
            Member reportedMember = reportedReply.getMember();
            reportedMember.addReportedCount();
            replyRepository.deleteById(replyNo);

            replyReportList.forEach(replyReport -> replyReport.update(status));
        } else {
            status = ProcessingStatus.REJECTED;
            reportedReply.resetReportedCount();

            replyReportList.forEach(replyReport -> replyReport.update(status));
        }

        return replyReportList.stream()
                .map(HandleReplyReportResponse::from)
                .toList();
    }

    // 댓글 신고 삭제
    @Transactional
    public DeleteReplyReportResponse deleteReplyReport(Long replyReportNo) {
        replyReportRepository.deleteById(replyReportNo);

        return DeleteReplyReportResponse.from(replyReportNo);
    }
}
