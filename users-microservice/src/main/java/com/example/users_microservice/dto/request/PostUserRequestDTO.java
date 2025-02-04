package com.example.users_microservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostUserRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
}
