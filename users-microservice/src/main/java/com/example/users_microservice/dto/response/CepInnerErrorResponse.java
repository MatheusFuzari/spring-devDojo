package com.example.users_microservice.dto.response;

import lombok.Builder;

@Builder
public record CepInnerErrorResponse(String name, String message, String service) {

}