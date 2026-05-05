package com.lokeshtr.task15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class Task15Application {

    public static void main(String[] args) {
        SpringApplication.run(Task15Application.class, args);
    }

}
