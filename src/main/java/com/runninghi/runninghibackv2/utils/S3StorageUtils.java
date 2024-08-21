package com.runninghi.runninghibackv2.utils;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.google.common.io.Files;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class S3StorageUtils {

    private AmazonS3Client amazonS3Client;
    private static final int SECURE_STRING_BYTE_SIZE = 16; // 16 byte -> 영문 + 숫자 조합 22자리

    @Value("${cloud.aws.s3.bucket}")
    private static String bucketName;


    public String uploadFile(MultipartFile file, String key) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, key).toString();
    }

    public String uploadFile(byte[] fileContent, String key) throws IOException {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        objectMetadata.setContentLength(fileContent.length);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, new ByteArrayInputStream(fileContent), objectMetadata);
        amazonS3Client.putObject(putObjectRequest);

        return amazonS3Client.getUrl(bucketName, key).toString();
    }

    public String buildKey(String dirName, MultipartFile file) {

        String extension = extractFileExtension(file);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());

        return dirName + "/" +
                SecureStringUtils.buildSecureString(SECURE_STRING_BYTE_SIZE) + "_" + date + extension;
    }

    private String extractFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return Objects.requireNonNull(fileName).substring(fileName.lastIndexOf("."));
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = File.createTempFile("temp", "." + extractFileExtension(file));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }

    private void copyFile(String sourceKey, String targetKey) {
        amazonS3Client.copyObject(new CopyObjectRequest(bucketName, sourceKey, bucketName, targetKey));
    }

    public void moveFileOnS3(String sourceKey, String targetKey) {
        copyFile(sourceKey, targetKey);
        deleteFile(sourceKey);
    }

    public void deleteFile(String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }

    public byte[] downloadFile(String key) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(bucketName, key);
        S3ObjectInputStream out = s3Object.getObjectContent();
        return IOUtils.toByteArray(out);
    }

    public String getEncodedFileName(String key) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

}
