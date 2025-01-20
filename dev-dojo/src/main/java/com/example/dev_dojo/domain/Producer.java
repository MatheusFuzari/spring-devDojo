package com.example.dev_dojo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Producer {
    private Long id;
    //Em caso de divergencia entre os valores recebidos da requisição
    @JsonProperty("name")
    private String name;
    private LocalDateTime createdAt;
    private static List<Producer> producers = new ArrayList<>();

    static {
        producers.addAll(List.of(
                Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(),
                Producer.builder().id(4L).name("Ghibli Studios").createdAt(LocalDateTime.now()).build()));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}
