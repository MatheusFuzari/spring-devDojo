package com.example.dev_dojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AnimePutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;
    @NotBlank(message = "The field 'anime' is required")
    private String anime;
}
