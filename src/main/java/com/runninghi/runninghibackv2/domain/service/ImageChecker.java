package com.runninghi.runninghibackv2.domain.service;

import com.runninghi.runninghibackv2.common.exception.custom.ImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class ImageChecker {

    private final static int IMAGE_MAX_LENGTH = 6;

    public String checkImageFile(String fileName) {
        if (isFileNameInvalid(fileName)) {
            log.error("파일 이름이 유효하지 않습니다. fileName: {}", fileName);
            throw new ImageException.InvalidFileName("파일 이름이 유효하지 않습니다.", fileName);
        }

        String extension = getFileExtension(fileName);

        if (isImageExtension(extension)) {
            return extension;
        } else {
            log.error("지원하는 이미지 파일이 아닙니다. fileName: {}", fileName);
            throw new ImageException.UnSupportedImageTypeException("지원하는 이미지 파일이 아닙니다.", fileName);
        }
    }

    public void checkMaxLength(List<MultipartFile> imageFiles) {

        int requestListLength = imageFiles.size();
        if (IMAGE_MAX_LENGTH < requestListLength) {
            throw new ImageException.InvalidImageLength("이미지 업로드 개수는 " + IMAGE_MAX_LENGTH + "개 이하 입니다.");
        }

    }

    public String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        return fileName.substring(dotIndex + 1);
    }

    private boolean isFileNameInvalid(String fileName) {
        return fileName == null || fileName.isBlank() || !fileName.contains(".");
    }

    private boolean isImageExtension(String extension) {

        String[] imageExtensions = {"jpg", "jpeg", "png", "gif", "bmp", "heic", "heif"};

        for (String ext : imageExtensions) {
            if (extension.equalsIgnoreCase(ext)) {
                return true;
            }
        }

        return false;
    }

    public String getFileNameFromUrl(String url) {
        if (url == null || url.lastIndexOf('.') == -1) {
            return null; // 확장자를 찾을 수 없으면 null 반환
        }

//        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);

        return url.substring(url.lastIndexOf('/') + 1);
    }

    public boolean isSameImage(String image1, String image2) {
        return Objects.equals(image1, image2);
    }

    public boolean isHeifOrHeic(String fileExtension) {
        return "heif".equalsIgnoreCase(fileExtension) || "heic".equalsIgnoreCase(fileExtension);
    }
}
