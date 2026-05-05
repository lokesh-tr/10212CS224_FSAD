package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MyApplication {

	@RequestMapping("/")
	String home() {
		return "Hello World!";
	}

    @RequestMapping("/about")
    String about() {
        return "About this program";
    }

	public static void main(String[] args) {
		SpringApplication.run(MyApplication.class, args);
	}

}