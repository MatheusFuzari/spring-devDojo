package com.example.dev_dojo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("greetings")
public class HelloController {

    @GetMapping("hi")
    public String hi(){
        return "Hi";
    }

    @PostMapping("how")
    public  Long save(String name) {
        return ThreadLocalRandom.current().nextLong(1, 1000);
    }
}
