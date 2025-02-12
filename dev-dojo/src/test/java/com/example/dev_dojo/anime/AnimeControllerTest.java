package com.example.dev_dojo.anime;

import com.example.dev_dojo.commons.AnimeUtils;
import com.example.dev_dojo.commons.FileUtils;
import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.producer.ProducerRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Stream;


@WebMvcTest(controllers = AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"com.example.dev_dojo.anime", "com.example.dev_dojo.commons"})
@Slf4j
class AnimeControllerTest {
    private static final String URL = "/v1/animes";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AnimeRepository repository;

    @MockitoBean
    ProducerRepository producerRepository;

    public List<Anime> animeList;

    @Autowired
    FileUtils fileUtils;

    @Autowired
    AnimeUtils animeUtils;

    @BeforeEach
    void init(){
        this.animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes findAll should return all animes when name is null")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenNameIsNull() throws Exception{
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("/animes/get-animes-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=x findAll should return empty list when name is not fond")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNameIsNull() throws Exception{
        BDDMockito.when(repository.findByAnime("x")).thenReturn(List.of());

        var response = fileUtils.readResourceFile("/animes/get-animes-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name","Shingeki"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=Berserk findName should return an anime in a list when name is found")
    @Order(3)
    void findByName_ReturnAnime_WhenNameIsFound() throws Exception{
        String name = "Berserk";
        BDDMockito.when(repository.findByAnime(name)).thenReturn(List.of(animeList.getFirst()));

        var response = fileUtils.readResourceFile("/animes/get-animes-berserk-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 findName should return a empty list when name is null")
    @Order(4)
    void findById_ReturnAnime_WhenIdIsFound() throws Exception{
        var response = fileUtils.readResourceFile("/animes/get-anime-by-id-200.json");
        var id = 1L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.of(animeList.getFirst()));


        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 findByID throws Not Found when id is not found")
    @Order(5)
    void findById_ThrowsNotFound_WhenIdIsNotFound() throws Exception{
        var response = fileUtils.readResourceFile("/animes/get-anime-by-id-404.json");
        var id = 99L;

        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());


        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("POST v1/animes save creates an anime")
    @Order(6)
    void save_CreateAnime_WhenSuccessful() throws Exception{
        var request = fileUtils.readResourceFile("/animes/post-request-anime-200.json");
        var response = fileUtils.readResourceFile("/animes/post-response-anime-201.json");

        var animeToSave = animeUtils.newAnimeToSave();

        log.debug("Anime to save: "+ animeToSave);

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(animeToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes update updates an anime")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful() throws Exception{
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(animeList.getFirst()));

        var request = fileUtils.readResourceFile("/animes/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes update throws not found when anime in not found")
    @Order(8)
    void update_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception{
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var request = fileUtils.readResourceFile("/animes/put-request-anime-404.json");
        var response = fileUtils.readResourceFile("/animes/put-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("DELETE v1/animes/1 delete removes an anime when anime exists")
    @Order(9)
    void delete_RemovesAnime_WhenSuccessful() throws Exception{
        var id = 1L;

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(animeList.getFirst()));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("DELETE v1/animes/99 throws Not Found when anime is not found")
    @Order(10)
    void delete_ThrowsNotFound_WhenAnimeIsNotFound() throws Exception{
        var id = 99L;

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        var response = fileUtils.readResourceFile("/animes/delete-anime-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postBadRequestSource")
    @DisplayName("POST v1/animes returns bad request when fields are empty")
    @Order(11)
    void save_ReturnsBadRequest_WhenFieldsAreEmpty(String filename, List<String> errors) throws Exception{

        var request = fileUtils.readResourceFile("/animes/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseError = mvcResult.getResolvedException();

        Assertions.assertThat(responseError).isNotNull();

        Assertions.assertThat(responseError.getMessage()).contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putBadRequestSource")
    @DisplayName("PUT v1/animes returns bad request when fields are empty")
    @Order(12)
    void update_ReturnsBadRequest_WhenFieldsAreEmpty(String filename, List<String> errors) throws Exception{

        var request = fileUtils.readResourceFile("/animes/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseError = mvcResult.getResolvedException();

        Assertions.assertThat(responseError).isNotNull();

        Assertions.assertThat(responseError.getMessage()).contains(errors);
    }

    private static Stream<Arguments> postBadRequestSource() {
        var commonErrors = commonErrors();

        return Stream.of(
                Arguments.of("post-request-anime-blank-fields-400.json", commonErrors),
                Arguments.of("post-request-anime-empty-fields-400.json", commonErrors)
        );
    }

    private static Stream<Arguments> putBadRequestSource() {
        var idNullError = "The field 'id' cannot be null";

        var commonErrors = commonErrors();
        commonErrors.add(idNullError);

        var idErros = Collections.singletonList(idNullError);

        return Stream.of(
                Arguments.of("put-request-anime-blank-fields-400.json", commonErrors),
                Arguments.of("put-request-anime-empty-fields-400.json", commonErrors),
                Arguments.of("put-request-anime-null-id-400.json", idErros)
        );

    }

    private static List<String> commonErrors(){
        var animeFieldError = "The field 'anime' is required";

        return new ArrayList<>(List.of(animeFieldError));
    }
}