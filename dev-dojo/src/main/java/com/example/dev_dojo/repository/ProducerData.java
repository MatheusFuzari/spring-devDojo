package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Producer;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class ProducerData {

    private final List<Producer> producers = new ArrayList<>();

    {
        producers.addAll(List.of(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(), Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(), Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(), Producer.builder().id(4L).name("Ghibli Studios").createdAt(LocalDateTime.now()).build()));
    }


}
