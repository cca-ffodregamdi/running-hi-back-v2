package com.runninghi.runninghibackv2.domain.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ImageCheckerTest {

    @Autowired
    private ImageChecker imageChecker;

    @Test
    @DisplayName("이미지 개수 6개 이하일 때 메소드 통과 확인")
    void checkMaxLengthTest() {

        // given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("file1", "filename1.txt", "text/plain", "some xml".getBytes()));
        multipartFiles.add(new MockMultipartFile("file2", "filename2.txt", "text/plain", "some text".getBytes()));

        // when & then
        Assertions.assertThatCode(
                () -> imageChecker.checkMaxLength(multipartFiles)
        ).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("이미지 개수가 6개 이상일 때 예외 처리 확인")
    void checkMaxLengthTest_exception() {

        // given
        List<MultipartFile> multipartFiles = new ArrayList<>();
        multipartFiles.add(new MockMultipartFile("file1", "filename1.txt", "text/plain", "some xml".getBytes()));
        multipartFiles.add(new MockMultipartFile("file2", "filename2.txt", "text/plain", "some text".getBytes()));
        multipartFiles.add(new MockMultipartFile("file3", "filename3.txt", "text/plain", "some text".getBytes()));
        multipartFiles.add(new MockMultipartFile("file4", "filename4.txt", "text/plain", "some text".getBytes()));
        multipartFiles.add(new MockMultipartFile("file5", "filename5.txt", "text/plain", "some text".getBytes()));
        multipartFiles.add(new MockMultipartFile("file6", "filename6.txt", "text/plain", "some text".getBytes()));
        multipartFiles.add(new MockMultipartFile("file7", "filename7.txt", "text/plain", "some text".getBytes()));

        // when & then
        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> imageChecker.checkMaxLength(multipartFiles));
    }

}