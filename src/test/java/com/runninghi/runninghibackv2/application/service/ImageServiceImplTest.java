package com.runninghi.runninghibackv2.application.service;

import com.runninghi.runninghibackv2.common.utils.S3StorageUtils;
import com.runninghi.runninghibackv2.domain.entity.Image;
import com.runninghi.runninghibackv2.domain.repository.ImageRepository;
import com.runninghi.runninghibackv2.domain.service.ImageChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageServiceImplTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageChecker imageChecker;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private S3StorageUtils s3StorageUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("이미지 리스트 업로드 - success")
    void uploadImageList() throws IOException {
        // Given
        BufferedImage bufferedImage1 = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        BufferedImage bufferedImage2 = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage1, "jpg", baos1);
        ImageIO.write(bufferedImage2, "png", baos2);

        MultipartFile file1 = new MockMultipartFile("file1", "test1.jpg", "image/jpeg", baos1.toByteArray());
        MultipartFile file2 = new MockMultipartFile("file2", "test2.png", "image/png", baos2.toByteArray());

        List<MultipartFile> fileList = Arrays.asList(file1, file2);
        Long memberNo = 1L;
        String dirName = "images/";

        when(s3StorageUtils.buildKey(any(), anyString()))
                .thenReturn("key1")
                .thenReturn("key2");
        when(s3StorageUtils.uploadFile((byte[]) any(), anyString()))
                .thenReturn("url1")
                .thenReturn("url2");
        when(imageChecker.getFileExtension(anyString()))
                .thenReturn("jpg")
                .thenReturn("png");

        // When
        List<String> result = imageService.uploadImageList(fileList, memberNo, dirName);

        // Then
        assertEquals(2, result.size());
        assertEquals("url1", result.get(0));
        assertEquals("url2", result.get(1));

        verify(imageChecker, times(1)).checkMaxLength(fileList);
        verify(s3StorageUtils, times(2)).buildKey(any(), anyString());
        verify(s3StorageUtils, times(2)).uploadFile((byte[]) any(), anyString());
        verify(imageChecker, times(2)).getFileExtension(anyString());
    }

    @Test
    @DisplayName("이미지 업로드 - success")
    void uploadImage() throws IOException {
        // Given
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        byte[] imageBytes = baos.toByteArray();

        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);
        Long memberNo = 1L;
        String dirName = "images/";

        when(s3StorageUtils.buildKey(any(), anyString())).thenReturn("key");
        when(s3StorageUtils.uploadFile((byte[]) any(), anyString())).thenReturn("url");
        when(imageChecker.getFileExtension(anyString())).thenReturn("jpg");

        // When
        String result = imageService.uploadImage(file, memberNo, dirName);

        // Then
        assertEquals("url", result);
        verify(s3StorageUtils, times(1)).buildKey(any(), anyString());
        verify(s3StorageUtils, times(1)).uploadFile((byte[]) any(), anyString());
    }


    @Test
    @DisplayName("이미지 리스트 저장 테스트 - success")
    void saveImageList() {
        // Given
        List<String> imageUrlList = Arrays.asList("url1", "url2");

        // When
        imageService.saveImageList(imageUrlList);

        // Then
        verify(imageRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("이미지 저장 테스트 - success")
    void saveImage() {
        // Given
        String imageUrl = "url";

        // When
        imageService.saveImage(imageUrl);

        // Then
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    @DisplayName("이미지 리스트 삭제 테스트 - success")
    void deleteImageList() {
        // Given
        List<String> imageUrlList = Arrays.asList("url1", "url2");
        List<Image> existingImages = Arrays.asList(
                Image.builder().imageUrl("url1").build(),
                Image.builder().imageUrl("url2").build()
        );
        when(imageRepository.findByImageUrlIn(imageUrlList)).thenReturn(existingImages);
        when(imageRepository.deleteAllByImageUrlIn(anyList())).thenReturn(2);

        // When
        imageService.deleteImageList(imageUrlList);

        // Then
        verify(s3StorageUtils, times(2)).deleteFile(anyString());
        verify(imageRepository, times(1)).deleteAllByImageUrlIn(anyList());
    }

    @Test
    @DisplayName("이미지 삭제 테스트 - success")
    void deleteImage() {
        // Given
        String imageUrl = "url";
        Image image = Image.builder().imageUrl(imageUrl).build();

        when(imageRepository.findImageByImageUrl(imageUrl)).thenReturn(Optional.of(image));
        doNothing().when(s3StorageUtils).deleteFile(imageUrl);

        // When
        imageService.deleteImageFromStorage(imageUrl);
        imageService.deleteImageFromDB(imageUrl);

        // Then
        // Verify the interactions with the mocks
        verify(s3StorageUtils, times(1)).deleteFile(imageUrl);
        verify(imageRepository, times(1)).delete(image);
    }

    @Test
    @DisplayName("이미지 삭제 테스트 - 이미지가 존재하지 않을 때 예외 발생")
    void deleteImage_NotFound() {
        // Given
        String imageUrl = "nonExistentUrl";

        when(imageRepository.findImageByImageUrl(imageUrl)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            imageService.deleteImageFromDB(imageUrl);
        });

        // Verify that the delete method was never called
        verify(imageRepository, never()).delete(any(Image.class));
    }

    @Test
    @DisplayName("이미지 삭제 스케줄러 테스트 - success")
    void deleteUnassignedImages() {
        // Given
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(7);
        List<String> unassignedImageUrlList = Arrays.asList("url1", "url2");
        when(imageRepository.findUnassignedImagesBeforeDate(any(LocalDateTime.class))).thenReturn(unassignedImageUrlList);
        when(imageRepository.deleteAllByImageUrlIn(unassignedImageUrlList)).thenReturn(2);

        // When
        imageService.deleteUnassignedImages();

        // Then
        verify(imageRepository, times(1)).findUnassignedImagesBeforeDate(any(LocalDateTime.class));
        verify(s3StorageUtils, times(2)).deleteFile(anyString());
        verify(imageRepository, times(1)).deleteAllByImageUrlIn(unassignedImageUrlList);
    }
}