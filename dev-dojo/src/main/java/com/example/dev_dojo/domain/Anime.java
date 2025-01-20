package com.example.dev_dojo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Anime {
    private Long id;
    private String anime;

    private static List<Anime> animes = new ArrayList<>();

    static {
        animes.addAll(List.of(new Anime(1L, "Berserk"),
                new Anime(2L, "Jujutsu Kaisen"),
                new Anime(3L, "Soul Eater"),
                new Anime(4L, "Bleach")));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
