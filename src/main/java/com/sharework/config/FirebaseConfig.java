package com.sharework.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account.path}")
    private String path;

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public void initializeFirebase() {
        try {
            Resource resource = resourceLoader.getResource(path);

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
                .build();

            FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);

            log.info("initializeFirebase done: " + firebaseApp.getName() + " With serviceAccount json: " + path);
        } catch (FileNotFoundException e) {
            log.error("initializeFirebase FileNotFoundException: " + e);
        } catch (IOException e) {
            log.error("initializeFirebase IOException: " + e);
        }
    }
}
