package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.data.domain.Sort;

@Getter
public class GetReportedReplySearchRequest {

        @PositiveOrZero(message = "0 또는 자연수만 입력 가능합니다.")
        @Schema(description = "현재 페이지", example = "1")
        int page;

        @Positive
        @Schema(description = "페이지 당 표시 게시글 수", example = "10")
        int size;

        @Pattern(regexp = "desc|asc", message = "정렬 조건이 맞지 않습니다.")
        @Schema(description = "정렬 조건", example = "desc")
        Sort.Direction sortDirection;

        @Pattern(regexp = "INPROGRESS|ACCEPTED|REJECTED", message = "신고 상태 조건이 맞지 않습니다.")
        @Schema(description = "신고 상태", example = "INPROGRESS")
        ProcessingStatus reportStatus;

        @Size(max = 10, message = "10자 이내로 입력해주세요.")
        @Pattern(regexp = "/^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$/") // 특수문자 입력 방지
        @Schema(description = "닉네임 검색 내용", example = "러너1")
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
