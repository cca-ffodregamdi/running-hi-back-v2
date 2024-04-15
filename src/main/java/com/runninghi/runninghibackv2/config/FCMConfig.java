package com.runninghi.runninghibackv2.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FCMConfig {

    @Value("${fcm.certification}")
    private String certificationPath;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {

        ClassPathResource resource =
                new ClassPathResource(certificationPath);
        InputStream serviceAccount = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // 설명. FirebaseApp 인스턴스가 존재하는지 확인
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        return FirebaseMessaging.getInstance(firebaseApp);
    }

}