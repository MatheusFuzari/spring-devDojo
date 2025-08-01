package com.example.users_microservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
