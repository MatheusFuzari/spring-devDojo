package com.example.users_microservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PutUserRequestDTO {
    @NotNull(message = "The field 'id' cannot be null")
    @Schema(description = "User's id", example = "1")
    private Long id;

    @NotBlank(message = "The field 'firstName' is required")
    @Schema(description = "User's first name", example = "Rin")
    private String firstName; // null, "", " "

    @NotBlank(message = "The field 'lastName' is required")
    @Schema(description = "User's last name", example = "Itoshi")
    private String lastName;

    @NotBlank(message = "The field 'email' is required")
    @Email(message = "'email' is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Schema(description = "User's e-mail. Must be unique", example = "rin.itoshi@frombluelock.com")
    private String email;
}
