package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Anime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnimeHardCodedRepository {

    private final AnimeData animes;

    public List<Anime> findAll() {
        return animes.getAnimes();
    }

    public List<Anime> findByName(String anime) {
        return animes.getAnimes().stream().filter(a -> a.getAnime().equalsIgnoreCase(anime)).toList();
    }

    public Optional<Anime> findById(Long id) {
        return animes.getAnimes().stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime) {
        animes.getAnimes().add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        animes.getAnimes().remove(anime);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }
}
