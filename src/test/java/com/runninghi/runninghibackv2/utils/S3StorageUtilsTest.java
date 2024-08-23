package com.runninghi.runninghibackv2.utils;

import com.runninghi.runninghibackv2.common.utils.S3StorageUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class S3StorageUtilsTest {

    @Autowired
    private S3StorageUtils s3StorageUtils;

    @Test
    @DisplayName("S3 Key builder 테스트")
    void test_s3_build_key() {

        // given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());

        // when
        String key = s3StorageUtils.buildKey(file, "/test");
        System.out.println(key);

        // then
        Assertions.assertThat(key.contains(date)).isTrue();
        Assertions.assertThat(key.length()).isEqualTo(41);
    }

    @Test
    @DisplayName("extractKeyFromUrl 테스트 - success")
    public void testExtractKeyFromUrl() {
        // given
        String testUrl = "https://runninghi-dev2.s3.ap-northeast-2.amazonaws.com/profile/default_profile.png";
        String expectedKey = "profile/default_profile.png";

        // when
        String extractedKey = s3StorageUtils.extractKeyFromUrl(testUrl);

        // then
        Assertions.assertThat(expectedKey).isEqualTo(extractedKey);
    }

    @Test
    @DisplayName("extractKeyFromUrl 테스트 - 잘못된 url 형식일 때 예외를 발생시키는지 확인")
    public void testInvalidUrlFormat() {

        // given
        String invalidUrl = "not-a-url";

        // when & then
        Assertions.assertThatThrownBy(() -> s3StorageUtils.extractKeyFromUrl(invalidUrl))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("extractKeyFromUrl 테스트 - 키가 없는 URL 형식으로 테스트")
    public void testUrlWithoutKey() {

        // given
        String urlWithoutKey = "https://bucket.s3.amazonaws.com/";

        // when & then
        Assertions.assertThatThrownBy(() -> s3StorageUtils.extractKeyFromUrl(urlWithoutKey))
                        .isInstanceOf(IllegalArgumentException.class);
    }

}