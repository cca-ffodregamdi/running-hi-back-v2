package com.runninghi.runninghibackv2.application.dto.post.request;

import java.util.List;

public record PostImageRequest(
        List<String> imageUrlList
) {
}
