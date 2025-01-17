package com.example.dev_dojo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("v1")
@Slf4j
public class AnimeController {

    @GetMapping("/animes")

    public List<String> animesList() {
        log.info(Thread.currentThread().getName());
        return List.of("A", "B", "C", "D", "E", "F", "G");
    }
}
