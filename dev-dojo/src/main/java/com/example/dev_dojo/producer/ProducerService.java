package com.example.dev_dojo.producer;

import com.example.dev_dojo.domain.Producer;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProducerService {

    private final ProducerRepository repository;

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
        assertProducerExists(producerToUpdate.getId());
        repository.save(producerToUpdate);
    }

    public void assertProducerExists(Long id) {
        findById(id);
    }
}
