package com.runninghi.runninghibackv2.application.dto.member.response;

public record GetIsLocationResponse(
        boolean isLocation
) {
    public static GetIsLocationResponse of(boolean isLocation) {
        return new GetIsLocationResponse(isLocation);
    }
}
