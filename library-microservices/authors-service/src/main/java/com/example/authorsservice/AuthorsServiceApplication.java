package com.example.authorsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AuthorsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorsServiceApplication.class, args);
    }

}
