package com.runninghi.runninghibackv2.application.dto.reply.request;

import com.runninghi.runninghibackv2.domain.enumtype.ProcessingStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public class GetReportedReplySearchRequest {

        @PositiveOrZero(message = "0 또는 자연수만 입력 가능합니다.")
        @Schema(description = "현재 페이지", example = "1")
        int page;

        @Positive
        @Schema(description = "페이지 당 표시 게시글 수", example = "10")
        int size;

        @Pattern(regexp = "desc|asc|ASC|DESC", message = "정렬 조건이 맞지 않습니다.")
        @Schema(description = "정렬 조건", example = "desc")
        String sortDirection;

        @Pattern(regexp = "ALL|INPROGRESS|ACCEPTED|REJECTED", message = "신고 상태 조건이 맞지 않습니다.")
        @Schema(description = "신고 상태", example = "INPROGRESS")
        String reportStatus;

        @Size(max = 10, message = "10자 이내로 입력해주세요.")
        @Pattern(regexp = "^[ ㄱ-ㅎ가-힣a-zA-Z0-9]*$", message = "한글과 영문, 숫자만 입력 가능합니다.") // 특수문자 입력 방지, 빈 문자, 공백 허용
        @Schema(description = "닉네임 검색 내용", example = " ")
        String search;

        public GetReportedReplySearchRequest (Integer page, Integer size, String sortDirection, String reportStatus, String search) {
                this.page = page == null ? 0 : page;
                this.size = size == null ? 10 : size;
                this.sortDirection = sortDirection;
                this.reportStatus = reportStatus;
                this.search = search;


        }
}
