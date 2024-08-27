package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.application.dto.image.response.ImageTarget;
import com.runninghi.runninghibackv2.common.utils.S3StorageUtils;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import com.runninghi.runninghibackv2.domain.service.ImageChecker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageChecker imageChecker;
    private final ImageRepository imageRepository;

    private final S3StorageUtils s3StorageUtils;

    private final static int IMAGE_RESIZE_TARGET_WIDTH = 650;

    // 저장 경로 - image / memberNo / UUID + 업로드 시간.jpg
    // 이미지 업로드 시 미리 보기만! -> post 생성 시 이미지 업로드 방식으로 할 지 => DB url

    private Image getImageByImageUrl(String imageUrl) {
        Image image = imageRepository.findImageByImageUrl(imageUrl)
                .orElseThrow(() -> {
                    log.error("DB에 존재하지 않는 이미지입니다. imageUrl: {}", imageUrl);
                    throw new EntityNotFoundException();
                });
        log.info("성공적으로 이미지를 조회하였습니다. imageUrl: {}", imageUrl);
        return image;
    }

    private Image getImageByTargetNo(Long targetNo) {
        Image image = imageRepository.findImageByTargetNo(targetNo)
                .orElseThrow(() -> {
                    log.error("{}번이 할당된 이미지는 DB에 존재하지 않습니다.", targetNo);
                    throw new EntityNotFoundException();
                });
        log.info("성공적으로 이미지를 조회하였습니다. targetNo: {}", targetNo);
        return image;
    }

    /**
     * 이미지를 단순히 S3에 업로드하고 반환된 url을 String 형태로 반환해주는 메서드입니다.
     *
     * @param fileList MultipartFile List
     * @param memberNo 회원 식별 값. s3 업로드 시에 경로에 사용.(경로 통일)
     * @param dirName  S3에 업로드할 때 지정되는 Path
     * @return 이미지 url 리스트
     * @throws IOException file -> byte[] 변환 과정에서 IO 예외 발생 처리
     */
    @Override
    public List<String> uploadImageList(List<MultipartFile> fileList, Long memberNo, String dirName) throws IOException {

        imageChecker.checkMaxLength(fileList);

        List<String> imageUrlList = new ArrayList<>();
        for (MultipartFile file : fileList) {
            imageUrlList.add(uploadImage(file, memberNo, dirName));
        }
        return imageUrlList;
    }

    /**
     * 이미지 한 장을 단순히 S3에 업로드하고 반환된 url을 String 형태로 반환해주는 메서드입니다.
     *
     * @param multipartFile MultipartFile
     * @param memberNo      회원 식별 값. s3 업로드 시에 경로에 사용.(경로 통일)
     * @param dirName       S3에 업로드할 때 지정되는 Path
     * @return 이미지 url
     * @throws IOException file -> byte[] 변환 과정에서 IO 예외 발생 처리
     */
    @Override
    public String uploadImage(MultipartFile multipartFile, Long memberNo, String dirName) throws IOException {

        // 이미지 파일 유효성 검증
        imageChecker.checkImageFile(multipartFile.getOriginalFilename());

        dirName += memberNo;
        String key = s3StorageUtils.buildKey(multipartFile, dirName);
        byte[] resizedImage = resizeImage(multipartFile);

        return s3StorageUtils.uploadFile(resizedImage, key);
    }

    @Override
    public void saveImageList(List<String> imageUrlList) {
        List<Image> imageList = new ArrayList<>();

        for (String imageUrl : imageUrlList) {
            imageList.add(
                    Image.builder()
                            .imageUrl(imageUrl)
                            .build()
            );
        }

        imageRepository.saveAll(imageList);
        log.info("이미지 리스트가 DB에 추가되었습니다.");
    }

    @Override
    public void saveImage(String imageUrl) {

        imageRepository.save(Image.builder()
                .imageUrl(imageUrl)
                .build()
        );
        log.info("이미지가 DB에 추가되었습니다.");
    }

    @Override
    public void saveTargetNo(List<String> imageUrlList, ImageTarget imageTarget, Long targetNo) {

        for (String imageUrl : imageUrlList) {
            saveTargetNo(imageUrl, imageTarget, targetNo);
        }
    }

    @Override
    public void saveTargetNo(String imageUrl, ImageTarget imageTarget, Long targetNo) {

        String filename = imageChecker.getFileNameFromUrl(imageUrl);
        imageChecker.checkImageFile(filename);

        Image image = getImageByImageUrl(imageUrl);
        image.updateImageTarget(imageTarget);
        image.updateTargetNo(targetNo);
        log.info("{} 번의 이미지가 {} 의 {} 번의 엔테티로 할당되었습니다,", image.getId(), image.getImageTarget(), image.getTargetNo());
    }

    @Override
    public void updateImage(Long targetNo, String newImageUrl) {

        // 이미지 유효성 검증
        String filename = imageChecker.getFileNameFromUrl(newImageUrl);
        imageChecker.checkImageFile(filename);

        Image image = getImageByTargetNo(targetNo);
        String currentImageUrl = image.getImageUrl();

        if (imageChecker.isSameImage(currentImageUrl, newImageUrl)) {
            return;
        }

        deleteImageFromStorage(currentImageUrl);
        image.updateImageUrl(newImageUrl);
        log.info("{} - {}의 이미지가 변경되었습니다.", image.getImageTarget(), targetNo);
    }

    @Override
    public byte[] resizeImage(MultipartFile multipartFile) throws IOException {

        BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());
        if (originalImage == null) {
            throw new IOException();
        }
        BufferedImage resizedImage =
                Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_TO_WIDTH, IMAGE_RESIZE_TARGET_WIDTH, Scalr.THRESHOLD_QUALITY_BALANCED);
        String fileExtension = imageChecker.getFileExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, fileExtension, outputStream);

        log.info("성공적으로 이미지가 리사이징되었습니다.");
        return outputStream.toByteArray();
    }

    @Override
    public void deleteImageList(List<String> imageUrlList) {

        List<Image> existingImages = imageRepository.findByImageUrlIn(imageUrlList);

        List<String> existingUrls = existingImages.stream()
                .map(Image::getImageUrl)
                .toList();

        // S3에서 이미지 삭제
        for (String imageUrl : existingUrls) {
            s3StorageUtils.deleteFile(imageUrl);
        }

        // DB에서 이미지 정보 일괄 삭제
        int deletedCount = imageRepository.deleteAllByImageUrlIn(existingUrls);

        log.info("DB와 Storage 에서 {}개의 이미지가 삭제되었습니다.", deletedCount);
    }

    @Override
    public void deleteImageFromStorage(String imageUrl) {

        s3StorageUtils.deleteFile(imageUrl);
        log.info("{} 이미지가 Storage 에서 이미지가 삭제되었습니다.", imageUrl);
    }

    @Override
    public void deleteImageFromDB(String imageUrl) {
        Image image = getImageByImageUrl(imageUrl);
        imageRepository.delete(image);
        log.info("{} 이미지가 DB에서 삭제되었습니다.", imageUrl);
    }

    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시에 실행
    public void deleteUnassignedImages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7); // 일주일 동안 할당되지 않은 이미지 삭제
        log.info("{}일 이후의 할당되지 않은 사진들을 삭제합니다.", cutoffDate);
        List<String> unassignedImageUrlList = imageRepository.findUnassignedImagesBeforeDate(cutoffDate);

        // S3에서 이미지 삭제
        for (String url : unassignedImageUrlList) {
            s3StorageUtils.deleteFile(url);
        }

        int deletedCount = imageRepository.deleteAllByImageUrlIn(unassignedImageUrlList);
        log.info("DB와 Storage 에서 {}개의 이미지가 삭제되었습니다.", deletedCount);
    }
}
