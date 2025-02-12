package com.example.dev_dojo.anime;

import com.example.dev_dojo.domain.Anime;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {

    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByAnime(name);
    }

    public Anime findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anime Not Found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        var animeToDelete = findById(id);
        repository.delete(animeToDelete);
    }

    public void update(Anime animeToUpdate) {
        assertAnimeExists(animeToUpdate.getId());
        repository.save(animeToUpdate);
    }

    public void assertAnimeExists(Long id) {
        findById(id);
    }
}
