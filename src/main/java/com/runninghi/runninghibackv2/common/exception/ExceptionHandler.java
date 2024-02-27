package com.runninghi.runninghibackv2.common.exception;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({NoSuchElementException.class, BadRequestException.class})
    public ResponseEntity<ApiResult> handleBadRequestException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.BAD_REQUEST);
        return ResponseEntity.badRequest().body(apiResult);
    }

}
