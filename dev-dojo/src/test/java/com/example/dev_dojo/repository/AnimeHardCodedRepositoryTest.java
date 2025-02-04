package com.example.dev_dojo.repository;

import com.example.dev_dojo.commons.AnimeUtils;
import com.example.dev_dojo.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeHardCodedRepositoryTest {

    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;
    private List<Anime> animeList;
    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init(){
        this.animeList = animeUtils.newAnimeList();

        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);
    }

    @Test
    @DisplayName("findAll should return all animes when successful")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenSuccessful(){
        var animes = repository.findAll();

        Assertions.assertThat(animes).isNotEmpty().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findName should return one anime in list")
    @Order(2)
    void findByName_ReturnAnime_WhenSuccessful(){
        var expectedAnime = animeList.getFirst();
        var animeFound = repository.findByName(expectedAnime.getAnime());

        Assertions.assertThat(animeFound).isNotEmpty().contains(expectedAnime);
    }

    @Test
    @DisplayName("findName should return a empty list when name is null")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNull(){
        var animeFound = repository.findByName(null);

        Assertions.assertThat(animeFound).isEmpty();
    }

    @Test
    @DisplayName("findById should return one anime")
    @Order(4)
    void findById_ReturnAnime_WhenSuccessful(){
        var expectedAnime = animeList.getFirst();
        var animeFound = repository.findById(expectedAnime.getId());

        Assertions.assertThat(animeFound).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("save creates an anime")
    @Order(5)
    void save_CreateAnime_WhenSuccessful(){
        var newAnime = animeUtils.newAnimeToSave();
        var anime = repository.save(newAnime);

        Assertions.assertThat(anime).isEqualTo(newAnime).hasNoNullFieldsOrProperties();

        var animeFoundOptional = repository.findAll();

        Assertions.assertThat(animeFoundOptional).isNotEmpty().contains(newAnime);
    }

    @Test
    @DisplayName("delete removes an anime")
    @Order(6)
    void delete_RemovesAnime_WhenSuccessful(){
        var animeToDelete = animeList.getFirst();

        repository.delete(animeToDelete);

        Assertions.assertThat(this.animeList).doesNotContain(animeToDelete);

        var animeFoundOptional = repository.findAll();

        Assertions.assertThat(animeFoundOptional).doesNotContain(animeToDelete);
    }

    @Test
    @DisplayName("update updates an anime")
    @Order(7)
    void update_UpdateAnime_WhenSuccessful(){
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setAnime("Jujutsu Kaisen");

        repository.update(animeToUpdate);

        Assertions.assertThat(this.animeList).isNotEmpty().contains(animeToUpdate);

        var updatedAnime = repository.findById(animeToUpdate.getId());

        Assertions.assertThat(updatedAnime).isPresent();
        Assertions.assertThat(updatedAnime.get().getAnime()).isEqualTo(animeToUpdate.getAnime());
    }
}