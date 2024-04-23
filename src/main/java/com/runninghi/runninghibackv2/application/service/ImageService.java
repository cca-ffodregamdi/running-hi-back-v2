package com.runninghi.runninghibackv2.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.runninghi.runninghibackv2.application.dto.image.response.CreateImageResponse;
import com.runninghi.runninghibackv2.application.dto.image.response.DownloadImageResponse;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import com.runninghi.runninghibackv2.domain.service.ImageChecker;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3Client amazonS3Client;

    private final ImageChecker imageChecker;

    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final static int IMAGE_RESIZE_TARGET_WIDTH = 650;

    // 저장 경로 - memberNo / postNo / UUID + 업로드 시간.jpg
    // 이미지 업로드 시 미리 보기만! -> post 생성 시 이미지 업로드 방식으로 할 지 => DB url

    // 이미지 일단 업로드 -> memberNo / UUID + 업로드 시간.jpg

    @Transactional
    public List<CreateImageResponse> saveImages(List<MultipartFile> imageFiles, Long memberNo) {

        imageChecker.checkMaxLength(imageFiles); // 이미지 길이 제한 검사
        String dirName = String.valueOf(memberNo);

        List<Image> imageList = new ArrayList<>();
        for (String imageUrl : uploadImages(imageFiles, dirName)) {
            Image image = Image.builder()
                    .imageUrl(imageUrl)
                    .build();
            imageList.add(image);
        }

        return imageRepository.saveAll(imageList).stream().map(CreateImageResponse::fromEntity).toList();
    }

    public DownloadImageResponse downloadImage(String key) throws IOException {
        S3Object s3Object = amazonS3Client.getObject(bucketName, key);
        S3ObjectInputStream out = s3Object.getObjectContent();  // S3ObjectInputStream은 Java로 동작하는 InputStream 객체이다
        byte[] bytesFile = IOUtils.toByteArray(out);

        // 사진 명 인코딩 작업
        String fileName = URLEncoder.encode(key, StandardCharsets.UTF_8).replaceAll("\\+", "%20");  // URI에서 공백을 '%20'으로 표현함

        return new DownloadImageResponse(bytesFile, fileName);
    }

    private List<String> uploadImages(List<MultipartFile> imageFiles, String dirName){

        return imageFiles.stream().map(image -> {
            try {
                return uploadImage(image, dirName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private String uploadImage(MultipartFile imageFile, String dirName) throws IOException {

        String key = buildKey(dirName, Objects.requireNonNull(imageFile.getOriginalFilename()));
        InputStream convertedImg = resizeImage(imageFile, IMAGE_RESIZE_TARGET_WIDTH);

        try {
            return putImageToS3(key, convertedImg);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에 실패하였습니다.");
        }
    }

    private String putImageToS3(String key, InputStream convertedImg) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        amazonS3Client.putObject(bucketName, key, convertedImg, objectMetadata);

        return amazonS3Client.getUrl(bucketName, key).toString();

    }


    /**
     * 이미지 경로와 파일 이름을 설정하는 메소드입니다.
     * @param dirName 저장 경로 이름입니다.
     * @param fileName 저장되는 파일 명입니다.
     * @return key가 생성됩니다.
     */
    private String buildKey(String dirName, String fileName) {

        String extension = imageChecker.checkImageFile(fileName);   // 확장자 비교 후 반환

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sdf.format(new Date());

        String newFileName = UUID.randomUUID() + "_" + now;

        return dirName + "/" + newFileName + extension;
    }

    /**
     * MultipartFile에서 File로 변경해주는 메소드입니다.
     * @param imageFile 클라이언트로부터 받은 MultipartFile 입니다.
     * @return File 변환된 File입니다.
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile imageFile) throws IOException {

        File convertFile = new File(Objects.requireNonNull(imageFile.getOriginalFilename()));

        if (convertFile.createNewFile()) {
            try (FileOutputStream out = new FileOutputStream(convertFile)) {
                out.write(imageFile.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    private InputStream resizeImage(MultipartFile multipartFile, int targetWidth) throws IOException {

        BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
        BufferedImage resizedImage = Scalr.resize(originalImage, targetWidth);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);
        byte[] resizedImageByte = outputStream.toByteArray();

        return new ByteArrayInputStream(resizedImageByte);
    }
}
