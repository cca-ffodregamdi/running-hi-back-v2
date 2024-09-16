package com.runninghi.runninghibackv2.common.exception;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.runninghi.runninghibackv2.common.exception.custom.*;
import com.runninghi.runninghibackv2.common.response.ApiResult;
import com.runninghi.runninghibackv2.common.response.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
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
        ApiResult apiResult = ApiResult.error(ErrorCode.KAKAO_OAUTH_FAIL.getStatus(), ErrorCode.KAKAO_OAUTH_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(KakaoOauthUnlinkException.class)
    public ResponseEntity<ApiResult> handleKakaoUnlinkException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.KAKAO_OAUTH_UNLINK_FAIL.getStatus(), ErrorCode.KAKAO_OAUTH_UNLINK_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(KakaoOauthProfileException.class)
    public ResponseEntity<ApiResult> handleKakaoProfileException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.KAKAO_OAUTH_PROFILE_FAIL.getStatus(), ErrorCode.KAKAO_OAUTH_UNLINK_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AppleOauthException.class)
    public ResponseEntity<ApiResult> handleAppleOauthException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.APPLE_OAUTH_FAIL.getStatus(), ErrorCode.APPLE_OAUTH_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AppleOauthClientSecretException.class)
    public ResponseEntity<ApiResult> handleAppleOauthClientSecretException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.APPLE_OAUTH_CLIENT_SECRET_FAIL.getStatus(), ErrorCode.APPLE_OAUTH_CLIENT_SECRET_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AppleOauthTokenException.class)
    public ResponseEntity<ApiResult> handleAppleOauthTokenException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.APPLE_OAUTH_TOKEN_FAIL.getStatus(), ErrorCode.APPLE_OAUTH_TOKEN_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AppleOauthClaimsException.class)
    public ResponseEntity<ApiResult> handleAppleOauthClaimsException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.APPLE_OAUTH_CLAIMS_FAIL.getStatus(), ErrorCode.APPLE_OAUTH_CLAIMS_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AppleOauthUnlinkException.class)
    public ResponseEntity<ApiResult> handleAppleOauthUnlinkException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.APPLE_OAUTH_UNLINK_FAIL.getStatus(), ErrorCode.APPLE_OAUTH_UNLINK_FAIL.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(FeedbackInvalidDataException.class)
    public ResponseEntity<ApiResult> handleFeedbackInvalidDataException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.FEEDBACK_INVALID_DATA.getStatus(), ErrorCode.FEEDBACK_INVALID_DATA.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AdminUnauthorizedException.class)
    public ResponseEntity<ApiResult> handleAdminUnauthorizedException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ADMIN_UNAUTHORIZED.getStatus(), ErrorCode.ADMIN_UNAUTHORIZED.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(AdminInvalidInvitationCodeException.class)
    public ResponseEntity<ApiResult> handleAdminInvalidInvitationCodeException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ADMIN_SIGNUP_INVALID_INVITATION.getStatus(), ErrorCode.ADMIN_SIGNUP_INVALID_INVITATION.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }


    @ExceptionHandler(AdminInvalidPasswordException.class)
    public ResponseEntity<ApiResult> handleAdminInvalidPasswordException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.ADMIN_INVALID_PASSWORD.getStatus(), ErrorCode.ADMIN_INVALID_PASSWORD.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }


    @ExceptionHandler(MemberInvalidDataException.class)
    public ResponseEntity<ApiResult> handleMemberInvalidDataException() {
        ApiResult apiResult = ApiResult.error(ErrorCode.MEMBER_INVALID_DATA.getStatus(), ErrorCode.MEMBER_INVALID_DATA.getMessage());
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
    public ResponseEntity<ApiResult> handleIOException(IOException e) {
        ApiResult apiResult = ApiResult.error(ErrorCode.INTER_SERVER_ERROR.getStatus(), e.getMessage());
        return ResponseEntity.status(apiResult.status()).body(apiResult);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(ErrorCode.BAD_REQUEST.getStatus(), errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResult<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResult<Void>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResult.error(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(FirebaseMessagingException.class)
    public ResponseEntity<ApiResult<Void>> handleFirebaseMessagingException(FirebaseMessagingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.error(ErrorCode.FCM_SEND_FAIL));
    }

    @ExceptionHandler(S3UploadException.class)
    public ResponseEntity<ApiResult<Void>> handleS3ImageUploadException(S3UploadException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResult.error(ErrorCode.INTER_SERVER_ERROR));
    }

    @ExceptionHandler(value = {
            CustomEntityNotFoundException.class,
            ImageException.InvalidFileName.class,
            ImageException.UnSupportedImageTypeException.class,
            ImageException.InvalidImageLength.class
    })
    public ResponseEntity<ApiResult<Void>> handleCustomBadRequestException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResult.error(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

}
