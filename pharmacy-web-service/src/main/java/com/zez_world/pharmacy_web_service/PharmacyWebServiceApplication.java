package com.zez_world.pharmacy_web_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PharmacyWebServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmacyWebServiceApplication.class, args);
    }
}