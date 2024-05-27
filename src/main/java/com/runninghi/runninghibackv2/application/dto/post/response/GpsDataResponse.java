package com.runninghi.runninghibackv2.application.dto.post.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GpsDataResponse(

        @Schema(example = "[[126.981996,37.473919],[126.981994,37.473919],[126.981991,37.473919]]")
        List<double[]> coordinateList
) {
}
