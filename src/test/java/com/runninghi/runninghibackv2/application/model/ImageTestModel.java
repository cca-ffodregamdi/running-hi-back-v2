package com.runninghi.runninghibackv2.application.model;

import com.runninghi.runninghibackv2.domain.entity.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageTestModel {

    public static Image create() {
        return Image.builder()
                .imageUrl("test")
                .build();
    }
    public static List<Image> create(int i) {
        List<Image> imageList = new ArrayList<>();
        for(int j=0; j<i; j++) {
            imageList.add(
                    Image.builder()
                            .imageUrl("test" + j + ".png")
                            .build()
            );
        }
        return imageList;
    }

    public static List<String> createImageUrl(int i) {
        List<String> imageUrlList = new ArrayList<>();
        for (int j=0; j<i; j++) {
            imageUrlList.add("test" + j + ".png");
        }
        return imageUrlList;
    }

}
