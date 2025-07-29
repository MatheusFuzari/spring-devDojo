package com.example.dev_dojo.anime;

import com.dev_dojo.api.AnimeControllerApi;
import com.dev_dojo.dto.AnimeGetResponse;
import com.dev_dojo.dto.AnimePostRequest;
import com.dev_dojo.dto.AnimePutRequest;
import com.dev_dojo.dto.PageAnime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AnimeController implements AnimeControllerApi {

    private final AnimeMapper MAPPER;

    private final AnimeService service;

    @Override
    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {
        log.debug("End-point to all animes, with param {}", name);

        var animes = service.findAll(name);
        List<AnimeGetResponse> animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponseList);
    }

    @Override
    @GetMapping(value = "/paginated", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all animes paged",
            description = "Get animes by page, size and/or sort",
            responses = {
                    @ApiResponse(
                            description = "Get animes paged",
                            responseCode = "200",
                            useReturnTypeSchema = true //Get the method return type.
                    )
            }
    )
    public ResponseEntity<PageAnime> findAllAnimesPaged(@ParameterObject final Pageable pageable) {
        var animePaginated = service.findAllPaginated(pageable);
        var pagedAnime = MAPPER.toPageAnimeGetResponse(animePaginated);

        return ResponseEntity.status(HttpStatus.OK).body(pagedAnime);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findAnimeById(@PathVariable Long id) {
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


