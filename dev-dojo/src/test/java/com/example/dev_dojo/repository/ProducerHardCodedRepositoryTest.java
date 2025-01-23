package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerHardCodedRepositoryTest {
    //Mockito faz uma injeção de dependencias
    //Classe alvo
    @InjectMocks
    private ProducerHardCodedRepository repository;

    //Caso encontre uma classe desse tipo, instânciar
    @Mock
    private ProducerData producerData;

    private final List<Producer> producers = new ArrayList<>();

    //Será executado antes de cada metodo.
    @BeforeEach
    void init() {
        producers.addAll(List.of(Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build(), Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build(), Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build(), Producer.builder().id(4L).name("Ghibli Studios").createdAt(LocalDateTime.now()).build()));

        //Quando o metodo getProducers() for chaamdo, utilizar esta variável
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    }

    @Test
    @DisplayName("findAll returns a list with all producers")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenSuccessful() {
        //Calls BBDMockito

        var producers = repository.findAll();

        //Assertions.assertThat(producers).isNotNull().hasSize(this.producers.size());
        Assertions.assertThat(producers).isNotNull().hasSameElementsAs(this.producers);
    }

    @Test
    @DisplayName("findById return one producer")
    @Order(2)
    void findById_ReturnsOneProducer_WhenSuccessful() {
        //Calls BBDMockito
        var expectedProducer = this.producers.getFirst();
        var producer = repository.findById(expectedProducer.getId());
        //Assertions.assertThat(producer).isPresent().get().isEqualTo(expectedProducer);
        Assertions.assertThat(producer).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNull() {
        //Calls BBDMockito

        var producer = repository.findByName(null);

        Assertions.assertThat(producer).isEmpty();
    }

    @Test
    @DisplayName("findByName returns found producer in list when name is found")
    @Order(4)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
        //Calls BBDMockito
        var expectedProducer = this.producers.getFirst();
        var producer = repository.findByName(expectedProducer.getName());

        Assertions.assertThat(producer).contains(expectedProducer);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(5)
    void save_CreatesProducer_WhenSuccessful() {
        var newProducer = Producer.builder().id(5L).name("Ufutable").createdAt(LocalDateTime.now()).build();
        var producer = repository.save(newProducer);

        Assertions.assertThat(producer).isEqualTo(newProducer).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(newProducer.getId());
        //.IsPresent() é necessário pois o retorno é Optional, e o .contains faz o trabalho do .get() tambem
        Assertions.assertThat(producerSavedOptional).isPresent().contains(newProducer);
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(6)
    void delete_RemovesProduces_WhenSuccessful() {
        var producerToDelete = producers.getFirst();
        repository.delete(producerToDelete);

        Assertions.assertThat(this.producers).doesNotContain(producerToDelete);
    }

    @Test
    @DisplayName("update renews a producer")
    @Order(7)
    void update_RenewsProducer_WhenSuccessful() {
        var producerToUpdate = producers.getFirst();
        producerToUpdate.setName("Aniplex");

        repository.update(producerToUpdate);

        Assertions.assertThat(producers).isNotEmpty().contains(producerToUpdate);

        var producerUpdated = repository.findById(producerToUpdate.getId());

        Assertions.assertThat(producerUpdated).isPresent();
        Assertions.assertThat(producerUpdated.get().getName()).isEqualTo("Aniplex");

    }
}