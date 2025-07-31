package com.example.users_microservice.common;

import com.example.users_microservice.dto.response.CepErrorResponse;
import com.example.users_microservice.dto.response.CepGetResponse;
import com.example.users_microservice.dto.response.CepInnerErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CepUtils {
    public CepGetResponse newCepGetResponse(){
        return CepGetResponse.builder()
                .cep("00000000")
                .city("Campinas")
                .state("São Paulo")
                .neighborhood("Ouro Verde")
                .street("Rua 123")
                .service("viacep")
                .build();
    }

    public CepErrorResponse newCepErrorResponse() {
        var cepInnerErrorResponse = CepInnerErrorResponse.builder()
                .name("ServiceError")
                .message("CEP INVÁLIDO")
                .service("correios")
                .build();

        return CepErrorResponse.builder()
                .name("CepPromiseError")
                .message("Todos os serviços de CEP retornaram erro.")
                .type("service_error")
                .errors(List.of(cepInnerErrorResponse))
                .build();
    }
}
