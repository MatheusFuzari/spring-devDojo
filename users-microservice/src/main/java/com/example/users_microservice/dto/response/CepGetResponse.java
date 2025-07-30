package com.example.users_microservice.dto.response;

public record CepGetResponse(String cep, String state, String city, String neighborhood, String street, String service) {
}
