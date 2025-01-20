package com.example.dev_dojo.controller;


import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.mapper.ProducerMapper;
import com.example.dev_dojo.request.ProducerPostRequest;
import com.example.dev_dojo.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    @GetMapping()
    public List<Producer> producersList(@RequestParam(required = false) String name) {
        var producers = Producer.getProducers();
        if(name == null) return producers;

        return producers.stream().filter(a -> a.getName().equalsIgnoreCase(name)).toList();

    }

    @GetMapping("{id}")
    public Producer producerById(@PathVariable Long id){
        return Producer.getProducers().stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }

    // Produces -> O que este endpoint está gerando? (Content-Type)
    // Consumes -> O que este endpoint consome? (Content-type dos Headers)
    // Headers -> Adiciona os headers necessários para a requisição, na Annotation é obrigatório, no argumento seleciona todos os headers enviados.
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
    headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> createProducer(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {

        Producer producer = MAPPER.toProducer(producerPostRequest);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(MAPPER.toProducerGetResponse(producer));
    }
}
