package com.runninghi.runninghibackv2.application.dto.image.response;

public record DownloadImageResponse(
        byte[] imageByte,
        String imageFileName
) {
}
