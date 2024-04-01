package com.runninghi.runninghibackv2.reply.domain.repository;

import com.runninghi.runninghibackv2.reply.application.dto.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.reply.application.dto.response.GetReplyListResponse;
import org.springframework.data.domain.Page;

public interface ReplyQueryRepository {
    Page<GetReplyListResponse> findAllReportedByPageableAndSearch(GetReportedReplyRequest request);
}
