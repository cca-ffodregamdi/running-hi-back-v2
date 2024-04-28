package com.runninghi.runninghibackv2.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageChecker {

    private final static int IMAGE_MAX_LENGTH = 6;

    public String checkImageFile(String fileName) {
        if (isFileNameInvalid(fileName)) {
            throw new IllegalArgumentException("파일 이름이 유효하지 않습니다.");
        }

        String extension = getFileExtension(fileName);

        if (isImageExtension(extension)) {
            return extension;
        } else {
            throw new IllegalArgumentException("이미지 파일이 아닙니다.");
        }
    }

    public void checkMaxLength(List<MultipartFile> imageFiles) {

        int requestListLength = imageFiles.size();
        if (IMAGE_MAX_LENGTH < requestListLength) throw new IllegalArgumentException("이미지 업로드 개수는 " + IMAGE_MAX_LENGTH + "개 이하 입니다.");

    }

    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(dotIndex + 1);
    }

    private boolean isFileNameInvalid(String fileName) {
        return fileName == null || fileName.isBlank() || !fileName.contains(".");
    }

    private boolean isImageExtension(String extension) {

        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp"};

        for (String ext : imageExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return false;
    }
}
