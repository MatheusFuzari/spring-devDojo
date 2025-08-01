package com.example.dev_dojo.producer;

import com.example.dev_dojo.domain.Producer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

  List<Producer> findByName(String name);
}
