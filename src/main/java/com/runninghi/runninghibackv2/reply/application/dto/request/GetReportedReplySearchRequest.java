package com.runninghi.runninghibackv2.reply.application.dto.request;

import com.runninghi.runninghibackv2.common.enumtype.ProcessingStatus;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class GetReportedReplySearchRequest {

        @PositiveOrZero(message = "0 또는 자연수만 입력 가능합니다.")
        int page;

        @Positive
        int size;

        @Pattern(regexp = "desc|asc", message = "정렬 조건이 맞지 않습니다.")
        Sort.Direction sortDirection;

        @Pattern(regexp = "INPROGRESS|ACCEPTED|REJECTED", message = "신고 상태 조건이 맞지 않습니다.")
        ProcessingStatus reportStatus;

        @Size(max = 10, message = "10자 이내로 입력해주세요.")
        @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/")
        String search;

        public GetReportedReplySearchRequest (Integer page, Integer size, Sort.Direction sortDirection,
                                              ProcessingStatus reportStatus, String search) {
                this.page = page == null ? 0 : page;
                this.size = size == null ? 10 : size;
                this.sortDirection = sortDirection == null ? Sort.Direction.DESC : Sort.Direction.ASC;
                this.reportStatus = reportStatus == null ? ProcessingStatus.INPROGRESS : reportStatus;
                this.search = search;


        }
}
