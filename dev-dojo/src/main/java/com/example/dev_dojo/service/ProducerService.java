package com.example.dev_dojo.service;

import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.repository.ProducerHardCodedRepository;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerHardCodedRepository repository;

    public List<Producer> findAll(String name){
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Producer findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producer Not Found"));
    }

    public Producer save(Producer producer) {
        return repository.save(producer);
    }

    public void delete(Long id) {
        Producer producer = findById(id);
        repository.delete(producer);
    }

    public void update(Producer producerToUpdate) {
        var producer = findById(producerToUpdate.getId());
        producerToUpdate.setCreatedAt(producer.getCreatedAt());
        repository.update(producerToUpdate);
    }

}
