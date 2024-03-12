package com.runninghi.runninghibackv2.common.exception;

import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResult> handleBadRequestException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.BAD_REQUEST);
        return ResponseEntity.badRequest().body(apiResult);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResult> handleAccessDeniedException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResult);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResult> handleEntityNotFoundException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ENTITY_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResult);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResult> handleEmptyResultDataAccessException() {
        return ResponseEntity.badRequest().body(ApiResult.error(ErrorCode.BAD_REQUEST));
    }
}
