package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Producer;
import external.dependecy.Connection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProducerHardCodedRepository {

    private final ProducerData producerData;

    @Qualifier(value = "connectionMongo")
    private final Connection connection;


    public List<Producer> findAll() {
        return producerData.getProducers();
    }

    public List<Producer> findByName(String name) {
        return producerData.getProducers().stream().filter(a -> a.getName().equalsIgnoreCase(name)).toList();
    }

    public Optional<Producer> findById(Long id) {
        return producerData.getProducers().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Producer save(Producer producer) {
        producerData.getProducers().add(producer);
        return producer;
    }

    public void delete(Producer producer) {
        producerData.getProducers().remove(producer);
    }

    public void update(Producer producer) {
        delete(producer);
        save(producer);
    }
}
