package com.example.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SuppressWarnings("unused")
public class IndexController {
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/")
    public String getHelloWorld() {
        return "hello";
    }
}
