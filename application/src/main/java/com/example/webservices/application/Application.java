package com.example.webservices.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.webservices")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
