package com.fireStore.fireStoreDemo;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class FireStoreDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FireStoreDemoApplication.class, args);
	}


	@Bean
	public Firestore getFirebaseInstance()
	{
		return FirestoreClient.getFirestore();
	}
}
