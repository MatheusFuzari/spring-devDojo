package com.example.users_microservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponseDTO {
    @Schema(description = "User's id", example = "99")
    private Long id;
    @Schema(description = "User's first name", example = "Yoichi")
    private String firstName;
    @Schema(description = "User's last name", example = "Isagi")
    private String lastName;
    @Schema(description = "User's e-mail. Must be unique", example = "yoichi.isagi@frombluelock.com")
    private String email;
}
