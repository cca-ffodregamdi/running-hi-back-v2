package com.runninghi.runninghibackv2.auth.aop;

import com.runninghi.runninghibackv2.auth.jwt.JwtTokenProvider;
import com.runninghi.runninghibackv2.domain.enumtype.Role;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizationAspect {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * @HasAccess 어노테이션이 붙은 메서드 실행 전 후에 권한을 확인하는 Around 어드바이스입니다.
     *
     * @param joinPoint 실행되는 조인 포인트
     * @return 조인 포인트의 실행 결과
     * @throws Throwable 실행 중 발생한 예외
     */
    @Around("@annotation(com.runninghi.runninghibackv2.common.annotations.HasAccess)")
    public Object hasAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No current ServletRequestAttribute");
        }

        HttpServletRequest request = attributes.getRequest();
        String accessToken = jwtTokenProvider.extractAccessTokenFromRequest(request);
        String role = jwtTokenProvider.getRoleFromToken(accessToken);

        // 권한 확인
        if (!Role.ADMIN.name().equals(role)) {
            throw new AccessDeniedException("Access Denied");
        }

        return joinPoint.proceed();
    }

}
