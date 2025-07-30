package com.example.users_microservice.controller;

import com.example.users_microservice.config.BrasilApiConfigurationProperties;
import com.example.users_microservice.dto.response.CepGetResponse;
import com.example.users_microservice.services.BrasilApiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/brasil-api")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
@Slf4j
public class BrasilApiController {
    private final BrasilApiService service;

    @GetMapping("/cep/{cep}")
    public ResponseEntity<CepGetResponse> brasilApi(@PathVariable String cep) {
        log.info("request received to find cep");
        var cepGetResponse = service.findCep(cep);

        return ResponseEntity.ok(cepGetResponse);
    }
}
