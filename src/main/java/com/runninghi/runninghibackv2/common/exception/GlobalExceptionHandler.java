package com.runninghi.runninghibackv2.common.exception;

import com.runninghi.runninghibackv2.common.exception.custom.InvalidTokenException;
import com.runninghi.runninghibackv2.common.exception.custom.KakaoOauthException;
import com.runninghi.runninghibackv2.common.exception.custom.SchedulingException;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResult> handleBadRequestException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.getStatus()).body(apiResult);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResult> handleAccessDeniedException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResult> handleEntityNotFoundException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ENTITY_NOT_FOUND);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiResult> handleEmptyResultDataAccessException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResult> handleIllegalStateException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.INTER_SERVER_ERROR);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResult> handleInvalidTokenException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.INVALID_TOKEN);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(KakaoOauthException.class)
    public ResponseEntity<ApiResult> handleKakaoLoginException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.KAKAO_OAUTH_FAIL);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResult> handleHandlerMethodValidationException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.VALIDATION_FAIL);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(SchedulingException.class)
    public ResponseEntity<ApiResult> handleSchedulingException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.SCHEDULING_FAIL);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResult> handleIOException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.INTER_SERVER_ERROR);
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(ErrorCode.BAD_REQUEST.getStatus(), errorMessage));
    }

}
