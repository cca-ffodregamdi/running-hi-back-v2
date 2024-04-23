package com.runninghi.runninghibackv2.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.runninghi.runninghibackv2.application.dto.image.response.CreateImageResponse;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import com.runninghi.runninghibackv2.domain.service.ImageChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

    public void downloadImage(String fileName) {
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
        File file = convert(imageFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

//        Image
        return null;
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
}
