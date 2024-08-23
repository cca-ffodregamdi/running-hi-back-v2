package com.runninghi.runninghibackv2.common.utils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3StorageUtils {

    private final AmazonS3Client amazonS3Client;
    private static final int SECURE_STRING_BYTE_SIZE = 16; // 16 byte -> 영문 + 숫자 조합 22자리

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    public String uploadFile(MultipartFile file, String key) throws IOException {

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, metadata);
            amazonS3Client.putObject(putObjectRequest);
            log.info("S3에 성공적으로 업로드되었습니다. Bucket: {}, Key: {}", bucketName, key);
            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (AmazonServiceException e) {
            log.error("S3에 업로드 중 오류가 발생하였습니다.. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("S3에 업로드 중 오류가 발생하였습니다.", e);
        }
    }

    public String uploadFile(byte[] fileContent, String key) throws IOException {

        log.info("이미지를 {}로 업로드합니다.", key);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        objectMetadata.setContentLength(fileContent.length);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent)) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            amazonS3Client.putObject(putObjectRequest);
            log.info("S3에 성공적으로 업로드되었습니다. Bucket: {}, Key: {}", bucketName, key);
            return amazonS3Client.getUrl(bucketName, key).toString();
        } catch (IOException e) {
            log.error("파일 변환 중 오류가 발생하였습니다. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("파일 변환 중 오류가 발생하였습니다.", e);
        } catch (AmazonServiceException e) {
            log.error("S3에 업로드 중 에러가 발생하였습니다. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    public String buildKey(MultipartFile file, String dirName) {

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

    /**
     * MultipartFile에서 File로 변경해주는 메소드입니다.
     * 추후에 File은 꼭 삭제해주어야 합니다.
     * @param file 클라이언트로부터 받은 MultipartFile 입니다.
     * @return File 변환된 File입니다.
     * @throws IOException
     */
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

    public void deleteFile(String fileUrl) {
        String key = extractKeyFromUrl(fileUrl);
        try {
            amazonS3Client.deleteObject(bucketName, key);
            log.info("S3에서 파일을 성공적으로 삭제하였습니다. Bucket: {}, Key: {}", bucketName, key);
        } catch (AmazonServiceException e) {
            log.error("삭제 중 오류가 발생하였습니다. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("Failed to delete file from S3", e);
        }
    }

    public String extractKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();

            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            if (path.isEmpty()) {
                throw new IllegalArgumentException("URL 내에 키 값이 포함되어야 합니다.");
            }

            return path;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("잘못된 URL입니다.");
        }
    }

    public byte[] downloadFile(String key) throws IOException {
        try {
            S3Object s3Object = amazonS3Client.getObject(bucketName, key);
            try (S3ObjectInputStream inputStream = s3Object.getObjectContent()) {
                byte[] content = IOUtils.toByteArray(inputStream);
                log.info("S3에서 성공적으로 다운로드하였습니다. Bucket: {}, Key: {}", bucketName, key);
                return content;
            }
        } catch (AmazonServiceException e) {
            log.error("다운로드 중 오류가 발생하였습니다. Bucket: {}, Key: {}", bucketName, key, e);
            throw new RuntimeException("다운로드 중 오류가 발생하였습니다.", e);
        }
    }

    public String getEncodedFileName(String key) {
        return URLEncoder.encode(key, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
    }

}
