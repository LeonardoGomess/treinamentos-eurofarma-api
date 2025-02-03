package br.com.eurotech.treinamentos.firebase.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

  @PostConstruct
  public void initializeFirebase() throws IOException {
    //String serviceAccountPath = System.getProperty("user.dir") + "/spring-firebase-key.json";
    String serviceAccountPath = "/etc/secrets/spring-firebase-key.json";
    FileInputStream serviceAccountStream = new FileInputStream(serviceAccountPath);

    FirebaseOptions options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
            .setStorageBucket("firabase-euro.appspot.com")
            .build();
    
    if(FirebaseApp.getApps().isEmpty()){
        FirebaseApp.initializeApp(options);
    }
  }

}