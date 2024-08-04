package com.runninghi.runninghibackv2.application.dto.member.response;

import org.locationtech.jts.geom.Point;

public record UpdateCurrentLocationResponse(
        Long memberNo,
        double latitude,
        double longitude
) {

    public static UpdateCurrentLocationResponse from(Long memberNo, Point geometry) {
        return new UpdateCurrentLocationResponse(memberNo, geometry.getY(), geometry.getX()); // longitude 경도 == x, longitude 위도 == y
    }
}
