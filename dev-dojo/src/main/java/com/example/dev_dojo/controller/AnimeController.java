package com.example.dev_dojo.controller;


import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.mapper.AnimeMapper;
import com.example.dev_dojo.request.AnimePostRequest;
import com.example.dev_dojo.request.AnimePutRequest;
import com.example.dev_dojo.response.AnimeGetResponse;
import com.example.dev_dojo.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.MAPPER;

    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> animesList(@RequestParam(required = false) String name) {
        log.debug("End-point to all animes, with param {}", name);

        var animes = service.findAll(name);
        List<AnimeGetResponse> animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponseList);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> animeById(@PathVariable Long id) {
        var anime = service.findById(id);
        var response = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping()
    public ResponseEntity<AnimeGetResponse> createAnime(@RequestBody AnimePostRequest request) {
        log.debug("End-point for saving animes {}", request);

        var anime = MAPPER.toAnime(request);
        service.save(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(MAPPER.toAnimeGetResponse(anime));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {
        log.debug("Deleting anime by id {}", id);

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping()
    public ResponseEntity<Void> updateAnime(@RequestBody AnimePutRequest putRequest) {
        log.debug("Update anime {}", putRequest);

        var animeToUpdate = MAPPER.toAnime(putRequest);
        service.update(animeToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}


