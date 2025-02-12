package com.example.dev_dojo.anime;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AnimePostRequest {
    @NotBlank(message = "The field 'anime' is required")
    private String anime;
}
