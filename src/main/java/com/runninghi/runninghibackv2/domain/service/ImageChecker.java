package com.runninghi.runninghibackv2.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class ImageChecker {

    private final static int IMAGE_MAX_LENGTH = 6;

    public boolean checkImageFile(String fileName) {

        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);

        // 이미지 확장자 목록
        String[] imageExtension = {"jpg", "jpeg", "png", "gif", "bmp"};
//        List<String> imageExtension = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp");

        for (String ext : imageExtension) {
            if(extension.equalsIgnoreCase(ext)) return true;
        }
//        imageExtension.contains()
        return false;
    }

    public int checkMaxLength(List<MultipartFile> imageFiles) {

        int requestListLength = imageFiles.size();
        if (IMAGE_MAX_LENGTH < requestListLength) throw new IllegalArgumentException("이미지 업로드 개수는 " + IMAGE_MAX_LENGTH + "개 이하 입니다.");

        return requestListLength;
    }
}
