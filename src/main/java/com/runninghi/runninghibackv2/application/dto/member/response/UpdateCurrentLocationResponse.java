package com.runninghi.runninghibackv2.application.dto.member.response;

import org.locationtech.jts.geom.Point;

public record UpdateCurrentLocationResponse(
        Long memberNo,
        double x,
        double y
) {

    public static UpdateCurrentLocationResponse from(Long memberNo, Point geometry) {
        return new UpdateCurrentLocationResponse(memberNo, geometry.getX(), geometry.getY());
    }
}
