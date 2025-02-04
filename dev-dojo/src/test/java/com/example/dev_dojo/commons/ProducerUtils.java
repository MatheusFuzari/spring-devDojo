package com.example.dev_dojo.commons;

import com.example.dev_dojo.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ProducerUtils {

    private final String dateTime = "2025-01-28T13:12:32.8572333";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
    private final LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

    public List<Producer> newProducerList() {
        return List.of(Producer.builder().id(1L).name("Mappa").createdAt(localDateTime).build(), Producer.builder().id(2L).name("Kyoto Animation").createdAt(localDateTime).build(), Producer.builder().id(3L).name("Madhouse").createdAt(localDateTime).build(), Producer.builder().id(4L).name("Ghibli Studios").createdAt(localDateTime).build());
    }

    public Producer newProducerToSave() {
        return Producer.builder().id(99L).name("Mappa").createdAt(localDateTime).build();
    }
}
