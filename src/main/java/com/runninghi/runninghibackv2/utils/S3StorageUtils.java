package com.runninghi.runninghibackv2.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3StorageUtils {

    private AmazonS3Client amazonS3Client;
    private static final int SECURE_STRING_BYTE_SIZE = 16; // 16 byte -> 영문 + 숫자 조합 22자리

    @Value("${cloud.aws.s3.bucket}")
    private static String bucketName;


    public String uploadFile(MultipartFile file, String key) {

        return null;
    }

    public String buildKey(String dirName, MultipartFile file) {

        String extension = extractFileExtension(file);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        return SecureStringUtils.buildSecureString(SECURE_STRING_BYTE_SIZE) + "_" + date + extension;
    }

    private String extractFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
    }

}
