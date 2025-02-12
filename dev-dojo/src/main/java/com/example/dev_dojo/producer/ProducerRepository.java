package com.example.dev_dojo.producer;

import com.example.dev_dojo.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    List<Producer> findByName(String name);
}
