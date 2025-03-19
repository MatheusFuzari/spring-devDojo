package com.example.users_microservice.dto.response;

import lombok.*;

@Builder
@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileResponseDTO {

    private Long id;
    private String name;
    private String description;
}
