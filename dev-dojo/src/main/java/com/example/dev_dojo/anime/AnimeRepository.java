package com.example.dev_dojo.anime;

import com.example.dev_dojo.domain.Anime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends JpaRepository<Anime, Long> {

  List<Anime> findByAnime(String name);
}
