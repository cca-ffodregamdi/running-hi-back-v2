package com.runninghi.runninghibackv2.domain.repository;

import com.runninghi.runninghibackv2.application.dto.reply.request.GetReplyListByMemberRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReplyListRequest;
import com.runninghi.runninghibackv2.application.dto.reply.request.GetReportedReplyRequest;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReplyListResponse;
import com.runninghi.runninghibackv2.application.dto.reply.response.GetReportedReplyResponse;
import com.runninghi.runninghibackv2.common.response.PageResultData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReplyQueryRepository {
    PageResultData<GetReportedReplyResponse> findAllReportedByPageableAndSearch(GetReportedReplyRequest request);

    PageResultData<GetReplyListResponse> findAllByMemberNoWithPaging(GetReplyListByMemberRequest request);

    List<GetReplyListResponse> findAllByMemberNo(Long memberNo, Sort sort);

    List<GetReplyListResponse> findAllByPostNo(GetReplyListRequest request);
}
