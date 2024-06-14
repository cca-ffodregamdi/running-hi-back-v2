package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReportedReplyResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import org.springframework.data.domain.Page;

public interface ReplyQueryRepository {
    PageResultData<GetReportedReplyResponse> findAllReportedByPageableAndSearch(GetReportedReplyRequest request);
}
