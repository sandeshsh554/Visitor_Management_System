package com.idbiintech.visitormgmtsystem;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;



@SpringBootApplication
public class VisitormanagementApplication implements WebMvcConfigurer {
	
	 
	  
    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException 
    {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "visitormanagement-5928f");
        return FirebaseMessaging.getInstance(app);
    }

	  
	  public void addResourceHandlers(ResourceHandlerRegistry registry) 
	  {
	  registry.addResourceHandler("/public/images/**").addResourceLocations("file:public/images/");
	  }
	  
	  	
	public static void main(String[] args) 
	{
		SpringApplication.run(VisitormanagementApplication.class, args);
	}
	
	
	   

}
