package org.example.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class FirebaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfiguration.class);

    String fileUrl = "hello";
    @PostConstruct
    public void init() {
        try {
            FileInputStream serviceAccount = new FileInputStream(fileUrl);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            logger.info("firebase initialized");
        } catch (Exception e) {
            logger.error("에러 설명", e);
        }
    }
}
