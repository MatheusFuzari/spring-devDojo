package com.example.dev_dojo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
