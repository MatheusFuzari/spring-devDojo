package com.example.dev_dojo.producer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProducerPostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
