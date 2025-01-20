package com.example.dev_dojo.controller;


import com.example.dev_dojo.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    @GetMapping()
    public List<Anime> animesList(@RequestParam(required = false) String name) {
        var animes = Anime.getAnimes();
        if(name == null) return animes;

        return animes.stream().filter(a -> a.getAnime().equalsIgnoreCase(name)).toList();

    }

    @GetMapping("{id}")
    public Anime animeById(@PathVariable Long id){
        return Anime.getAnimes().stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    @PostMapping()
    public Anime createAnime(@RequestBody Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(100_000));
        Anime.getAnimes().add(anime);
        return anime;
    }
}
