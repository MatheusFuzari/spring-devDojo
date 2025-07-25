package com.example.dev_dojo.producer;


import com.dev_dojo.api.ProducerControllerApi;
import com.dev_dojo.dto.ProducerGetResponse;
import com.dev_dojo.dto.ProducerPostRequest;
import com.dev_dojo.dto.ProducerPutRequest;
import com.example.dev_dojo.domain.Producer;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/producers")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProducerController implements ProducerControllerApi {

    private final ProducerMapper MAPPER;

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
    public ResponseEntity<List<ProducerGetResponse>> findAllProducers(@RequestParam(required = false) String name) {

        var producers = producerService.findAll(name);
        var producerResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.status(HttpStatus.OK).body(producerResponse);

    }

    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findProducerById(@PathVariable Long id){

        var producer = producerService.findById(id);
        var producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.status(HttpStatus.OK).body(producerGetResponse);
    }

    @Override
    @PostMapping
    public ResponseEntity<ProducerGetResponse> createProducer(ProducerPostRequest producerPostRequest) {
        log.debug("Requesto to POST producer"+ producerPostRequest);

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

    @PutMapping
    public ResponseEntity<Void> updateProducer(@RequestBody @Valid ProducerPutRequest putRequest) {
        log.debug("Update producer {}", putRequest);

        var producerUpdated = MAPPER.toProducer(putRequest);

        producerService.update(producerUpdated);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
