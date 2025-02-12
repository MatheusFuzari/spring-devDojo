package com.example.dev_dojo.producer;

import com.example.dev_dojo.commons.FileUtils;
import com.example.dev_dojo.commons.ProducerUtils;
import com.example.dev_dojo.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@WebMvcTest(controllers = ProducerController.class) //Sliced Test
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"com.example.dev_dojo.producer", "com.example.dev_dojo.commons"})
@AutoConfigureMockMvc
@Slf4j
//@ActiveProfiles("test")
class ProducerControllerTest {
    private static final String URL = "/v1/producers";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ProducerRepository repository;

    private List<Producer> producers = new ArrayList<>();
    @Autowired
    FileUtils fileUtils;
    @Autowired
    ProducerUtils producerUtils;


    //Será executado antes de cada metodo.
    @BeforeEach
    void init() {
        this.producers = producerUtils.newProducerList();
    }


    @Test
    @DisplayName("GET v1/producers returns a list with all producers when argument is null")
    @Order(1)
    void findAll_ReturnsAllProducers_WhenArgumentIsNull() throws Exception {
        BDDMockito.when(repository.findAll()).thenReturn(producers);

        var response = fileUtils.readResourceFile("producer/get-producer-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers?name=Mappa returns a list with found producer when argument exists")
    @Order(2)
    void findByName_ReturnsFoundProducerInList_WhenNameIsFound() throws Exception {
        BDDMockito.when(repository.findByName("Mappa")).thenReturn(List.of(producers.getFirst()));

        var response = fileUtils.readResourceFile("producer/get-producer-mappa-name-200.json");
        String name = "Mappa";

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers?name=x returns a empty list when argument exists but not found")
    @Order(3)
    void findByName_ReturnsEmptyList_WhenNameIsNotFound() throws Exception {
        BDDMockito.when(repository.findByName("x")).thenReturn(List.of());

        var response = fileUtils.readResourceFile("producer/get-producer-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", "x")).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/producers/id return one producer")
    @Order(4)
    void findById_ReturnsOneProducer_WhenSuccessful() throws Exception{
        var response = fileUtils.readResourceFile("producer/get-producer-by-id-200.json");
        Long id = 1L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(producers.getFirst()));

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @DisplayName("GET v1/producers/id throws producer not found when producer is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenProducerIsNotFound() throws Exception{
        BDDMockito.when(repository.findAll()).thenReturn(producers);
        Long id = 99L;

        var response = fileUtils.readResourceFile("/producer/get-producer-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    //it works on insomnia!
    @Test
    @DisplayName("POST v1/producers creates a producer")
    @Order(6)
    void save_CreatesProducer_WhenSuccessful()  throws Exception{
        var request = fileUtils.readResourceFile("producer/post-request-producer-200.json");
        var response = fileUtils.readResourceFile("producer/post-response-producer-201.json");

        var producerToSave = producerUtils.newProducerToSave();

        log.debug("Producer to save: " + producerToSave);

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .header("x-api-kei", "123")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
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
    @DisplayName("PUT v1/producers update throws not found when producer is not found")
    @Order(8)
    void update_ThrowsNotFound_WhenProducerIsNotFound() throws Exception{
        BDDMockito.when(repository.findAll()).thenReturn(producers);

        var request = fileUtils.readResourceFile("producer/put-request-producer-404.json");
        var response = fileUtils.readResourceFile("/producer/get-producer-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders
                .put(URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(request)
            )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));

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
    @DisplayName("DELETE v1/producers/{id} delete throws Not Found when userId is Not Found")
    @Order(10)
    void delete_ThrowsNotFound_WhenIdIsNotFound() throws Exception{
        var id = 99L;

        var response = fileUtils.readResourceFile("/producer/get-producer-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postBadRequestSource")
    @DisplayName("POST v1/producers returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String filename, List<String> errors)  throws Exception{
        var request = fileUtils.readResourceFile("producer/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseErrors = mvcResult.getResolvedException();

        Assertions.assertThat(responseErrors).isNotNull();

        Assertions.assertThat(responseErrors.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT v1/producers returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String filename, List<String> errors)  throws Exception{
        var request = fileUtils.readResourceFile("producer/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseErrors = mvcResult.getResolvedException();

        Assertions.assertThat(responseErrors).isNotNull();

        Assertions.assertThat(responseErrors.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postBadRequestSource() {
        var commonErrors = commonErrors();

        return Stream.of(
                Arguments.of("post-request-producer-blank-fields-400.json", commonErrors),
                Arguments.of("post-request-producer-empty-fields-400.json", commonErrors)
        );
    }

    private static Stream<Arguments> putBadRequestSource() {
        var idNullError = "The field 'id' cannot be null";
        var commonErrors = commonErrors();
        commonErrors.add(idNullError);
        var idErrors = Collections.singletonList(idNullError);

        return Stream.of(
                Arguments.of("put-request-producer-blank-fields-400.json", commonErrors),
                Arguments.of("put-request-producer-empty-fields-400.json", commonErrors),
                Arguments.of("put-request-producer-null-id-400.json", idErrors)
        );
    }

    private static List<String> commonErrors() {
        var nameError = "The field 'name' is required";

        return new ArrayList<>(List.of(nameError));
    }

}