package com.example.dev_dojo.anime;


import com.example.dev_dojo.domain.Anime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "1. Anime Controller")
public class AnimeController {

    private final AnimeMapper MAPPER;

    private final AnimeService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get all Animes",
            description = "Get all animes available in system",
            parameters = {
                    @Parameter(
                            in = ParameterIn.PATH,
                            description = "Anime name",
                            name = "name",
                            example = "Jujutsu Kaisen"
                    )
            },
            responses = {
                    @ApiResponse(
                            description = "List of all animes",
                            responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = AnimeGetResponse.class)))
                    )
            }
    )
    public ResponseEntity<List<AnimeGetResponse>> animesList(@RequestParam(required = false) String name) {
        log.debug("End-point to all animes, with param {}", name);

        var animes = service.findAll(name);
        List<AnimeGetResponse> animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);

        return ResponseEntity.status(HttpStatus.OK).body(animeGetResponseList);
    }

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
    public ResponseEntity<Page<Anime>> animesPaged(@ParameterObject @PageableDefault(size = 20, page = 0) Pageable page) {

        var animePaginated = service.findAllPaginated(page);
        return ResponseEntity.status(HttpStatus.OK).body(animePaginated);
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


