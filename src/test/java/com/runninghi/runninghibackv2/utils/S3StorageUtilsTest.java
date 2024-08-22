package com.runninghi.runninghibackv2.utils;

import com.runninghi.runninghibackv2.common.utils.S3StorageUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

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
        Assertions.assertThat(key.length()).isEqualTo(35);
    }

}