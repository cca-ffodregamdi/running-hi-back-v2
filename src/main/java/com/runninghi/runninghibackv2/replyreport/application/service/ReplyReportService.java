package com.runninghi.runninghibackv2.replyreport.application.service;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import com.runninghi.runninghibackv2.member.domain.aggregate.entity.Member;
import com.runninghi.runninghibackv2.reply.domain.aggregate.entity.Reply;
import com.runninghi.runninghibackv2.replyreport.application.dto.request.CreateReplyReportRequest;
import com.runninghi.runninghibackv2.replyreport.application.dto.response.CreateReplyReportResponse;
import com.runninghi.runninghibackv2.replyreport.application.dto.response.GetReplyReportResponse;
import com.runninghi.runninghibackv2.replyreport.application.dto.response.HandleReplyReportResponse;
import com.runninghi.runninghibackv2.replyreport.domain.aggregate.entity.ReplyReport;
import com.runninghi.runninghibackv2.replyreport.domain.repository.ReplyReportRepository;
import com.runninghi.runninghibackv2.replyreport.domain.service.ApiReplyReportService;
import com.runninghi.runninghibackv2.replyreport.domain.service.ReplyReportChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplyReportService {

    private final ReplyReportRepository replyReportRepository;
    private final ReplyReportChecker replyReportChecker;
    private final ApiReplyReportService apiReplyReportService;

    // 댓글 신고 저장
    @Transactional
    public CreateReplyReportResponse createReplyReport(Long memberNo, CreateReplyReportRequest request) {

        replyReportChecker.checkReplyReportValidation(request);

        Member reporter = apiReplyReportService.getMemberById(memberNo);
        Reply reportedReply = apiReplyReportService.getReplyById(request.reportedReplyNo());

        ReplyReport replyReport = ReplyReport.builder()
                .category(request.category())
                .content(request.content())
                .status(ProcessingStatus.INPROGRESS)
                .reporter(reporter)
                .reportedReply(reportedReply)
                .replyContent(reportedReply.getReplyContent())
                .isReplyDeleted(false)
                .build();

        replyReportRepository.save(replyReport);
        apiReplyReportService.addReportedCountToReply(reportedReply.getReplyNo());

        return CreateReplyReportResponse.from(replyReport);
    }

    // 댓글 신고 전체 조회
    @Transactional(readOnly = true)
    public List<GetReplyReportResponse> getReplyReports() {

        return replyReportRepository.findAll().stream()
                .map(GetReplyReportResponse::from)
                .toList();
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
    public Page<GetReplyReportResponse> getReplyReportScrollByReplyId(Long replyNo, Pageable pageable) {

        Reply reply = apiReplyReportService.getReplyById(replyNo);

        return replyReportRepository.findAllByReportedReply(reply, pageable)
                .map(GetReplyReportResponse::from);
    }

    // 댓글 신고 수락/거절 처리
    @Transactional
    public List<HandleReplyReportResponse> handleReplyReports(boolean isAccepted, Long replyNo) {

        Reply reply = apiReplyReportService.getReplyById(replyNo);

        List<ReplyReport> replyReportList = replyReportRepository.findAllByReportedReply(reply);
        ProcessingStatus status;

        if(isAccepted) {
            status = ProcessingStatus.ACCEPTED;
            Long reportedMemberNo = reply.getWriter().getMemberNo();
            apiReplyReportService.addReportedCountToMember(reportedMemberNo);

            replyReportList.forEach(replyReport -> replyReport.update(status, true, null));
            apiReplyReportService.deleteReplyById(replyNo);
        } else {
            status = ProcessingStatus.REJECTED;
            apiReplyReportService.resetReportedCountOfReply(replyNo);
            replyReportList.forEach(replyReport -> replyReport.update(status));
        }

        return replyReportList.stream()
                .map(HandleReplyReportResponse::from)
                .toList();
    }

    // 댓글 신고 삭제
    @Transactional
    public void deleteReplyReport(Long replyReportNo) {
        replyReportRepository.deleteById(replyReportNo);
    }
}
