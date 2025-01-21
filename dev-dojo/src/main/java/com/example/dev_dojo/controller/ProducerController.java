package com.example.dev_dojo.controller;


import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.mapper.ProducerMapper;
import com.example.dev_dojo.request.AnimePutRequest;
import com.example.dev_dojo.request.ProducerPostRequest;
import com.example.dev_dojo.request.ProducerPutRequest;
import com.example.dev_dojo.response.ProducerGetResponse;
import com.example.dev_dojo.service.ProducerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
public class ProducerController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTANCE;

    private final ProducerService producerService;

    /*
    * Pq passar o Autowired no construtor, não na variavel?
    * 1° Imutabilidade
    * 2° Testes
    * 3° Single responsability
    *
    * */
//    @Autowired
//    public ProducerController(ProducerService service) {
//        this.producerService = service;
//    }

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> producersList(@RequestParam(required = false) String name) {

        var producers = producerService.findAll(name);
        var producerResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.status(HttpStatus.OK).body(producerResponse);

    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> producerById(@PathVariable Long id){

        var producer = producerService.findById(id);
        var producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.status(HttpStatus.OK).body(producerGetResponse);
    }

    // Produces -> O que este endpoint está gerando? (Content-Type)
    // Consumes -> O que este endpoint consome? (Content-type dos Headers)
    // Headers -> Adiciona os headers necessários para a requisição, na Annotation é obrigatório, no argumento seleciona todos os headers enviados.
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE,
    headers = "x-api-key")
    public ResponseEntity<ProducerGetResponse> createProducer(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {

        Producer producer = MAPPER.toProducer(producerPostRequest);

        producerService.save(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(MAPPER.toProducerGetResponse(producer));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProducerById(@PathVariable Long id) {
        log.debug("Deleting producer by id {}", id);

        producerService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping()
    public ResponseEntity<Void> updateProducer(@RequestBody ProducerPutRequest putRequest) {
        log.debug("Update producer {}", putRequest);

        var producerUpdated = MAPPER.toProducer(putRequest);

        producerService.update(producerUpdated);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
