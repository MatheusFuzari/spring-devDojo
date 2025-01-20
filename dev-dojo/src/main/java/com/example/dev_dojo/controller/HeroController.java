package com.example.dev_dojo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/heroes")
public class HeroController {

    private static final List<String> HEROS = List.of("Guts","Kakashi","Ichigo","Thorfim");

    @GetMapping
    public List<String> getHeros(){
        return HEROS;
    }

    @GetMapping("/filter")
    public List<String> getHerosParam(@RequestParam(required = false, defaultValue = "") String name){
        return HEROS.stream().filter(s -> s.equalsIgnoreCase(name)).toList();
    }

    @GetMapping("/filterList")
    public List<String> getHerosParamList(@RequestParam List<String> names){
        return HEROS.stream().filter(names::contains).toList();
    }

    @GetMapping("{name}")
    public String findByName(@PathVariable String name){
        return HEROS.stream().filter(s -> s.equalsIgnoreCase(name)).findFirst().orElse("");
    }
}
