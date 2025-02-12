package com.example.dev_dojo.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProducerGetResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String name;
}
