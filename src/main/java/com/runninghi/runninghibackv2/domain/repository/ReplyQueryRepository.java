package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import org.springframework.data.domain.Page;

public interface ReplyQueryRepository {
    Page<GetReplyListResponse> findAllReportedByPageableAndSearch(GetReportedReplyRequest request);
}
