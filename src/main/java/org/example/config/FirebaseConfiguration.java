package org.example.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@Component // ✅ @Configuration → @Component 로 변경
public class FirebaseConfiguration implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfiguration.class);

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                ClassPathResource resource = new ClassPathResource("firebase/OzOfirebasePrivateKey.json");
                InputStream serviceAccount = resource.getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                logger.info("✅ Firebase initialized in ApplicationRunner");
            } else {
                logger.info("Firebase already initialized. Skipping.");
            }
        } catch (Exception e) {
            logger.error("🔥 Firebase 초기화 실패", e);
        }
    }
}
