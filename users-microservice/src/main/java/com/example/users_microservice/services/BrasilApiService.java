package com.example.users_microservice.services;

import com.example.users_microservice.config.BrasilApiConfigurationProperties;
import com.example.users_microservice.dto.response.CepGetResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrasilApiService {

    private final RestClient.Builder brasilApiClient;
    private final BrasilApiConfigurationProperties brasilApiConfigurationProperties;

    public CepGetResponse findCep(String cep) {
        return brasilApiClient.build()
                .get()
                .uri(brasilApiConfigurationProperties.cepUri(), cep)
                .retrieve()
                .body(CepGetResponse.class);
    }

}
