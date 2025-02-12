package com.example.dev_dojo.anime;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {

    private final AnimeMapper MAPPER;

    private final AnimeService service;

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
    public ResponseEntity<AnimeGetResponse> createAnime(@RequestBody @Valid AnimePostRequest request) {
        log.debug("End-point for saving animes {}", request);

        var anime = MAPPER.toAnime(request);
        service.save(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(MAPPER.toAnimeGetResponse(anime));
    }

    @PutMapping()
    public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest putRequest) {
        log.debug("Update anime {}", putRequest);

        var animeToUpdate = MAPPER.toAnime(putRequest);
        service.update(animeToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAnimeById(@PathVariable Long id) {
        log.debug("Deleting anime by id {}", id);

        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}


