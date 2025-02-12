package com.example.dev_dojo.anime;

import com.example.dev_dojo.domain.Anime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

    List<Anime> findByAnime(String name);
}
