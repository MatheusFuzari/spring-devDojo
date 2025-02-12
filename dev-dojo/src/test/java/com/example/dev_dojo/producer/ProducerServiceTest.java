package com.example.dev_dojo.producer;

import com.example.dev_dojo.commons.ProducerUtils;
import com.example.dev_dojo.domain.Producer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProducerServiceTest {
    @InjectMocks
    private ProducerService service;
    @Mock
    private ProducerRepository repository;
    private List<Producer> producerList;
    @InjectMocks
    private ProducerUtils producerUtils;

    @BeforeEach
    void init() {
        this.producerList = producerUtils.newProducerList();

    }

    @Test
    @DisplayName("findAll returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() {
        //Calls BBDMockito
        //Quando o metodo getProducers() for chaamdo, utilizar esta variável
        BDDMockito.when(repository.findAll()).thenReturn(producerList);

        var producers = service.findAll(null);

        //Assertions.assertThat(producers).isNotNull().hasSize(this.producers.size());
        org.assertj.core.api.Assertions.assertThat(producers).isNotNull().hasSameElementsAs(this.producerList);
    }

    @Test
    @DisplayName("findAll returns a list with found producer when argument exists")
    @Order(2)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() {
        //Calls BBDMockito
        var producer = producerList.getFirst();

        //Quando o metodo getProducers() for chaamdo, utilizar esta variável
        BDDMockito.when(repository.findByName(producer.getName())).thenReturn(producerList);

        var producers = service.findAll(producer.getName());

        //Assertions.assertThat(producers).isNotNull().hasSize(this.producers.size());
        org.assertj.core.api.Assertions.assertThat(producers).isNotNull().contains(producer);
    }

    @Test
    @DisplayName("findAll returns a empty list when argument exists but not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() {
        //Calls BBDMockito

        //Quando o metodo getProducers() for chaamdo, utilizar esta variável
        BDDMockito.when(repository.findByName("Shingeki")).thenReturn(Collections.emptyList());

        var producers = service.findAll("Shingeki");

        //Assertions.assertThat(producers).isNotNull().hasSize(this.producers.size());
        org.assertj.core.api.Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById return one producer")
    @Order(4)
    void findById_ReturnsOneProducer_WhenSuccessful() {
        //Calls BBDMockito
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.of(expectedProducer));

        var producer = service.findById(expectedProducer.getId());

        //Assertions.assertThat(producer).isPresent().get().isEqualTo(expectedProducer);
        org.assertj.core.api.Assertions.assertThat(producer).isEqualTo(expectedProducer);
    }

    @Test
    @DisplayName("findById throws producer not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        //Calls BBDMockito
        var expectedProducer = producerList.getFirst();
        BDDMockito.when(repository.findById(expectedProducer.getId())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException().isThrownBy(() -> service.findById(expectedProducer.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() {
        var newProducer = producerUtils.newProducerToSave();
        BDDMockito.when(repository.save(newProducer)).thenReturn(newProducer);

        var producer = service.save(newProducer);

        org.assertj.core.api.Assertions.assertThat(producer).isEqualTo(newProducer).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes a producer")
    @Order(7)
    void delete_RemovesProduces_WhenSuccessful() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.of(producerToDelete));
        BDDMockito.doNothing().when(repository).delete(producerToDelete);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(producerToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws a ResponseStatusException when userId is Not Found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var producerToDelete = producerList.getFirst();
        BDDMockito.when(repository.findById(producerToDelete.getId())).thenReturn(Optional.empty());


        org.assertj.core.api.Assertions.assertThatException().isThrownBy(() -> service.delete(producerToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update renews a producer")
    @Order(9)
    void update_RenewsProducer_WhenSuccessful() {
        var producerToUpdate = producerList.getFirst();
        producerToUpdate.setName("Aniplex");

        BDDMockito.when(repository.findById(producerToUpdate.getId())).thenReturn(Optional.of(producerToUpdate));
        BDDMockito.when(repository.save(producerToUpdate)).thenReturn(producerToUpdate);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(producerToUpdate));

    }

    @Test
    @DisplayName("update throws a ResponseStatusException when producer is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() {
        var producerToUpdate = producerList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException().isThrownBy(() -> service.update(producerToUpdate));

    }
}