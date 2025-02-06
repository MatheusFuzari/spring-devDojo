package com.example.users_microservice.dto.request;

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
    private Long id;
    @NotBlank(message = "The field 'firstName' is required")
    private String firstName; // null, "", " "
    @NotBlank(message = "The field 'lastName' is required")
    private String lastName;
    @NotBlank(message = "The field 'email' is required")
    @Email(message = "'email' is not valid", regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;
}
