package com.runninghi.runninghibackv2.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


/**
 * PageResult 클래스는 페이징된 API 응답을 위한 레코드 클래스입니다.
 *
 * @param <T> 페이지 내용의 타입을 나타냅니다.
 */
public record PageResult<T>(
        @Schema(description = "응답 시간", example = "2024-03-27T14:20:52.026425")
        LocalDateTime timeStamp,
        @Schema(description = "응답 상태", example = "OK")
        String status,
        @Schema(description = "응답 메시지", example = "성공적으로 조회되었습니다.")
        String message,
        @Schema(description = "페이징 응답 데이터")
        PageResultData<T> data
) {
    public PageResult(String status, String message, PageResultData<T> data) {
        this(LocalDateTime.now(), status, message, data);
    }

    public static <T> PageResult<T> success(String message, PageResultData<T> data) {
        return new PageResult<>("OK", message, data);
    }

    public static <T> PageResult<T> error(HttpStatus status, String message) {
        return new PageResult<>(status.name(), message, null);
    }

    public static <T> PageResult<T> error(HttpStatus status, String message, PageResultData<T> data) {
        return new PageResult<>(status.name(), message, data);
    }
}
