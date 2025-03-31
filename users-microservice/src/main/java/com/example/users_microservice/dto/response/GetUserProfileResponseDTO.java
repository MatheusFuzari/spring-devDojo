package com.example.users_microservice.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@With
@Data
@Builder
public class GetUserProfileResponseDTO {
    public record User(Long id, String firstName) {}
    public record Profile(Long id, String name) {}

    private Long id;
    private User user;
    private Profile profile;


}
