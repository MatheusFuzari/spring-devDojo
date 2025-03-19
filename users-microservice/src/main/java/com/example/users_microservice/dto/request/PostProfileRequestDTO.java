package com.example.users_microservice.dto.request;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
public class PostProfileRequestDTO {

    @NotBlank(message = "The field 'name' can not be null or empty")
    private String name;
    @NotBlank(message = "The field 'description' can not be null or empty")
    private String description;
}
