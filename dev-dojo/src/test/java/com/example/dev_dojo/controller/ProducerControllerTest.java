package com.example.dev_dojo.controller;

import com.example.dev_dojo.commons.FileUtils;
import com.example.dev_dojo.commons.ProducerUtils;
import com.example.dev_dojo.domain.Producer;
import com.example.dev_dojo.repository.ProducerData;
import com.example.dev_dojo.repository.ProducerHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(controllers = ProducerController.class) //Sliced Test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"com.example.dev_dojo"})
//@ActiveProfiles("test")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProducerData producerData;
    @MockitoSpyBean
    private ProducerHardCodedRepository repository;
    private List<Producer> producers = new ArrayList<>();
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private ProducerUtils producerUtils;


    //Será executado antes de cada metodo.
    @BeforeEach
    void init() {
        this.producers = producerUtils.newProducerList();
    }


    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers?name=Mappa returns a list with found producer when argument exists")
    @Order(2)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = fileUtils.readResourceFile("producer/get-producer-mappa-name-200.json");
        String name = "Mappa";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns a empty list when argument exists but not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");
        String name = "Mappa";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", "x")).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/id return one producer")
    @Order(4)
    void findById_ReturnsOneProducer_WhenSuccessful() throws Exception{
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);

        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
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

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer Not Found"));
    }


    //it works on insomnia!
    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful()  throws Exception{
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");

        var producerToSave = producerUtils.newProducerToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-kei", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/producers update renews a producer")
    @Order(7)
    void update_RenewsProducer_WhenSuccessful() throws Exception{
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(producers.getFirst()));

        var request = fileUtils.readResourceFile("producer/put-request-producer-200.json");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/producers update throws a ResponseStatusException when producer is not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenProducerIsNotFound() throws Exception{
        BDDMockito.when(producerData.getProducers()).thenReturn(producers);
        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Producer Not Found"));

    }

    @Test
    @DisplayName("DELETE v1/producers/{id} delete removes a producer")
    @Order(9)
    void delete_RemovesProduces_WhenSuccessful() throws Exception{
        var id = 1L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producers.getFirst()));
        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/producers/{id} delete throws a ResponseStatusException when userId is Not Found")
    @Order(10)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception{
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}