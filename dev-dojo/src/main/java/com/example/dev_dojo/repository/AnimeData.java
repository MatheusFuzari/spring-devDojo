package com.example.dev_dojo.repository;

import com.example.dev_dojo.domain.Anime;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class AnimeData {
    public final List<Anime> animes = new ArrayList<>();

    {
        animes.addAll(List.of(
                Anime.builder().id(1L).anime("Berserk").build(),
                Anime.builder().id(2L).anime("Jujutsu Kaisen").build(),
                Anime.builder().id(3L).anime("Soul Eater").build(),
                Anime.builder().id(4L).anime("Bleach").build()));
    }
}
