package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Producer;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ProducerHardCodedRepository {
    private static final List<Producer> PRODUCERS = new ArrayList<>();

    static {
        PRODUCERS.addAll(List.of(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(), Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(), Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(), Producer.builder().id(4L).name("Ghibli Studios").createdAt(LocalDateTime.now()).build()));
    }

    public List<Producer> findAll() {
        return PRODUCERS;
    }

    public List<Producer> findByName(String name) {
        return PRODUCERS.stream().filter(a -> a.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Producer> findById(Long id) {
        return PRODUCERS.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Producer save(Producer producer) {
        PRODUCERS.add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        PRODUCERS.remove(producer);
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }
}
