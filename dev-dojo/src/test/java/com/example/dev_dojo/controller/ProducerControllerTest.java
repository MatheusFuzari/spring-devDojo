package com.example.dev_dojo.controller;

import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.mapper.ProducerMapperImpl;
import com.example.dev_dojo.repository.ProducerData;
import com.example.dev_dojo.repository.ProducerHardCodedRepository;
import com.example.dev_dojo.service.ProducerService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ProducerController.class) //Sliced Test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Import({ProducerMapperImpl.class, ProducerService.class, ProducerHardCodedRepository.class, ProducerData.class})
@ComponentScan(basePackages = "com.example.dev_dojo")
class ProducerControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProducerData producerData;
    private final List<Producer> producers = new ArrayList<>();

    @Autowired
    private ResourceLoader resourceLoader;

    //Será executado antes de cada metodo.
    @BeforeEach
    void init() {
        String dateTime = "2025-01-28T13:12:32.8572333";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);

        producers.addAll(List.of(Producer.builder().id(1L).name("Mappa").createdAt(localDateTime).build(), Producer.builder().id(2L).name("Kyoto Animation").createdAt(localDateTime).build(), Producer.builder().id(3L).name("Madhouse").createdAt(localDateTime).build(), Producer.builder().id(4L).name("Ghibli Studios").createdAt(localDateTime).build()));

    }


    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers")).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers?name=Mappa returns a list with found producer when argument exists")
    @Order(2)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-mappa-name-200.json");
        String name = "Mappa";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", name)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns a empty list when argument exists but not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-x-name-200.json");
        String name = "Mappa";

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers").param("name", "x")).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/id return one producer")
    @Order(4)
    void findById_ReturnsOneProducer_WhenSuccessful() throws Exception{
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/id throws producer not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception{
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        Long id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/producers/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer Not Found"));
    }

    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful() {
        var newProducer = Producer.builder().id(5L).name("Ufutable").createdAt(LocalDateTime.now()).build();
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
    }

    private String readResourceFile(String filename) throws IOException {
        var file = resourceLoader.getResource("classpath:%s" .formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}