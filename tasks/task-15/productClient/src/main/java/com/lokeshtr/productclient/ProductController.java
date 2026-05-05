package com.lokeshtr.productclient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @GetMapping("product")
    public String msg() {
        return "This is the product page. You can get product list here";
    }
}
