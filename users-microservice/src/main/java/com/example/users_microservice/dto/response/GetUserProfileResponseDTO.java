package com.example.users_microservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@With
@Data
@Builder
public class GetUserProfileResponseDTO {

  private Long id;
  private User user;
  private Profile profile;

  public record User(Long id, String firstName) {

  }

  public record Profile(Long id, String name) {

  }


}
