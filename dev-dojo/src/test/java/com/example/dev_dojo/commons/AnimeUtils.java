package com.example.dev_dojo.commons;

import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.domain.Producer;
import org.h2.mvstore.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        return List.of(
                Anime.builder().id(1L).anime("Berserk").build(),
                Anime.builder().id(2L).anime("Jujutsu Kaisen").build(),
                Anime.builder().id(3L).anime("Soul Eater").build(),
                Anime.builder().id(4L).anime("Bleach").build());
    }

    public Anime newAnimeToSave() {
        return Anime.builder().id(99L).anime("Tokyo Ghoul").build();
    }
}
