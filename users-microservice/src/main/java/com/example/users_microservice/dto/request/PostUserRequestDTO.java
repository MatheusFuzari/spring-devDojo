package com.example.users_microservice.dto.request;

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
    private String firstName; // null, "", " "

    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;

    @NotBlank(message = "The field 'email' is required")
    @Email(message = "'email' is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
}
