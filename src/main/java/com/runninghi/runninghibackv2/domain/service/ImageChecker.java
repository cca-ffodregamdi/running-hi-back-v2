package com.runninghi.runninghibackv2.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Service
public class ImageChecker {

    private final static int IMAGE_MAX_LENGTH = 6;

    public String checkImageFile(String fileName) {

        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            throw new IllegalArgumentException("파일 이름에 확장자가 없습니다.");
        }

        String extension = fileName.substring(dotIndex + 1);

        // 이미지 확장자 목록
        String[] imageExtension = {"jpg", "jpeg", "png", "gif", "bmp"};

        for (String ext : imageExtension) {
            if(extension.equalsIgnoreCase(ext)) return extension;
        }

        throw new IllegalArgumentException("이미지 파일이 아닙니다.");
    }

    public int checkMaxLength(List<MultipartFile> imageFiles) {

        int requestListLength = imageFiles.size();
        if (IMAGE_MAX_LENGTH < requestListLength) throw new IllegalArgumentException("이미지 업로드 개수는 " + IMAGE_MAX_LENGTH + "개 이하 입니다.");

        return requestListLength;
    }
}
