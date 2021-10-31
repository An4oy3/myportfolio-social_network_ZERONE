package ru.skillbox.socialnetwork;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class SocialNetworkApplication {
	public static void main(String[] args) {
		SpringApplication.run(SocialNetworkApplication.class, args);
		log.info("Start application: " + LocalDateTime.now());
	}

}
