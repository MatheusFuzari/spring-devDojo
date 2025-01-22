package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Anime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimeHardCodedRepository {
    public static final List<Anime> ANIMES = new ArrayList<>();

    static {
        ANIMES.addAll(List.of(
                Anime.builder().id(1L).anime("Berserk").build(),
                Anime.builder().id(2L).anime("Jujutsu Kaisen").build(),
                Anime.builder().id(3L).anime("Soul Eater").build(),
                Anime.builder().id(4L).anime("Bleach").build()));
    }

    public List<Anime> findAll() {
        return ANIMES;
    }

    public List<Anime> findByName(String anime) {
        return ANIMES.stream().filter(a -> a.getAnime().equalsIgnoreCase(anime)).toList();
    }

    public Optional<Anime> findById(Long id) {
        return ANIMES.stream().filter(a -> a.getId().equals(id)).findFirst();
    }

    public Anime save(Anime anime) {
        ANIMES.add(anime);
        return anime;
    }

    public void delete(Anime anime) {
        ANIMES.remove(anime);
    }

    public void update(Anime anime) {
        delete(anime);
        save(anime);
    }
}
