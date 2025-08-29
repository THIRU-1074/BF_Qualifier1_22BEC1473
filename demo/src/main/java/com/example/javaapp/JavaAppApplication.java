package com.example.javaapp;

import com.example.javaapp.service.WebhookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class JavaAppApplication implements CommandLineRunner {

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(JavaAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        webhookService.executeFlow();
    }
}
