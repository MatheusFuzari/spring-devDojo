package com.example.dev_dojo.service;

import com.example.dev_dojo.commons.AnimeUtils;
import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.repository.AnimeHardCodedRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {

    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeHardCodedRepository repository;
    private List<Anime> animeList;
    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init(){
        this.animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll should return all animes when name is null")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenNameIsNull(){
        BDDMockito.when(repository.findAll()).thenReturn(animeList);
        var animes = service.findAll(null);

        org.assertj.core.api.Assertions.assertThat(animes).isNotEmpty().hasSameElementsAs(animeList);
    }


    @Test
    @DisplayName("findAll should return empty list when name is not fond")
    @Order(2)
    void findByName_ReturnEmptyList_WhenNameIsNull(){
        BDDMockito.when(repository.findByName("Shingeki")).thenReturn(List.of());
        var animes = service.findAll("Shingeki");

        org.assertj.core.api.Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("findName should return an anime in a list when name is found")
    @Order(3)
    void findByName_ReturnAnime_WhenNameIsFound(){
        var animeFound = animeList.getFirst();
        BDDMockito.when(repository.findByName(animeFound.getAnime())).thenReturn(List.of(animeFound));

        var anime = service.findAll(animeFound.getAnime());

        org.assertj.core.api.Assertions.assertThat(anime).isNotEmpty().contains(animeFound);
    }

    @Test
    @DisplayName("findName should return a empty list when name is null")
    @Order(4)
    void findById_ReturnAnime_WhenIdIsFound(){
        var animeFound = animeList.getFirst();
        BDDMockito.when(repository.findById(animeFound.getId())).thenReturn(Optional.of(animeFound));

        var anime = service.findById(animeFound.getId());

        org.assertj.core.api.Assertions.assertThat(anime).isEqualTo(animeFound);
    }

    @Test
    @DisplayName("findName should return a empty list when name is null")
    @Order(5)
    void findById_ReturnResponseStatusException_WhenIdIsNotFound(){
        var animeFound = animeList.getFirst();
        BDDMockito.when(repository.findById(animeFound.getId())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.findById(animeFound.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("save creates an anime")
    @Order(6)
    void save_CreateAnime_WhenSuccessful(){
        var newAnime = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(newAnime)).thenReturn(newAnime);

        var anime = repository.save(newAnime);

        org.assertj.core.api.Assertions.assertThat(anime).isEqualTo(newAnime).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete removes an anime when anime exists")
    @Order(7)
    void delete_RemovesAnime_WhenSuccessful(){
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));
        BDDMockito.doNothing().when(repository).delete(animeToDelete);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound(){
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates an anime")
    @Order(9)
    void update_UpdateAnime_WhenSuccessful(){
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setAnime("Jujutsu Kaisen");
        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.doNothing().when(repository).update(animeToUpdate);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime in not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound(){
        var animeToUpdate = animeList.getFirst();
        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}