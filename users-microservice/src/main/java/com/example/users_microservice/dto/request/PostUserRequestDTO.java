package com.example.users_microservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostUserRequestDTO {

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
