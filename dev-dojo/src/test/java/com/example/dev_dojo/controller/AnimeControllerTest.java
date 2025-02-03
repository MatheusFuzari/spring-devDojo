package com.example.dev_dojo.controller;

import com.example.dev_dojo.commons.AnimeUtils;
import com.example.dev_dojo.commons.FileUtils;
import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.repository.AnimeData;
import com.example.dev_dojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(AnimeController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.example.dev_dojo")
class AnimeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AnimeData animeData;
    @MockitoSpyBean
    private AnimeHardCodedRepository repository;
    public List<Anime> animeList = new ArrayList<>();
    @Autowired
    private FileUtils fileUtils;
    @Autowired
    private AnimeUtils animeUtils;

    @BeforeEach
    void init(){
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("GET v1/animes findAll should return all animes when name is null")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenNameIsNull() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("/animes/get-animes-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=x findAll should return empty list when name is not fond")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNameIsNull() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(List.of());

        var response = fileUtils.readResourceFile("/animes/get-animes-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name","Shingeki"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes?name=Berserk findName should return an anime in a list when name is found")
    @Order(3)
    void findByName_ReturnAnime_WhenNameIsFound() throws Exception{
        String name = "Berserk";
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("/animes/get-animes-berserk-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes").param("name",name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/1 findName should return a empty list when name is null")
    @Order(4)
    void findById_ReturnAnime_WhenIdIsFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("/animes/get-anime-by-id-200.json");
        var id = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("GET v1/animes/99 findName should return a empty list when name is null")
    @Order(5)
    void findById_ReturnResponseStatusException_WhenIdIsNotFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var response = fileUtils.readResourceFile("/animes/get-anime-by-id-200.json");
        var id = 99L;

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/animes/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime Not Found"));
    }

    @Test
    @DisplayName("POST v1/animes save creates an anime")
    @Order(6)
    void save_CreateAnime_WhenSuccessful() throws Exception{
        var response = fileUtils.readResourceFile("/animes/post-response-anime-201.json");
        var request = fileUtils.readResourceFile("/animes/post-request-anime-200.json");

        var producerToSave = animeUtils.newAnimeToSave();

        BDDMockito.when(repository.save(ArgumentMatchers.any())).thenReturn(producerToSave);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @DisplayName("PUT v1/animes update updates an anime")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = fileUtils.readResourceFile("/animes/put-request-anime-200.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("PUT v1/animes update throws ResponseStatusException when anime in not found")
    @Order(8)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() throws Exception{
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var request = fileUtils.readResourceFile("/animes/put-request-anime-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/animes")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Anime Not Found"));
    }

    @Test
    @DisplayName("DELETE v1/animes/1 delete removes an anime when anime exists")
    @Order(9)
    void delete_RemovesAnime_WhenSuccessful(){
    }
}