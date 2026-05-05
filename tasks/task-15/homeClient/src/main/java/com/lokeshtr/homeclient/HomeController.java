package com.lokeshtr.homeclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("home")
    public String msg() {
        return "I'm the home service, you can create a home page and display it.";
    }
}
