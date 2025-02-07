package com.example.dev_dojo.service;

import com.example.dev_dojo.domain.Anime;
import com.example.dev_dojo.repository.AnimeHardCodedRepository;
import com.exemple.dev_dojo.DefaultErrorMessage;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnimeService {

    private final AnimeHardCodedRepository repository;


    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findById(Long id) {
        var teste = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), "teste");

        log.debug("teste pacote: "+teste);

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
        repository.update(animeToUpdate);
    }

    public void assertAnimeExists(Long id) {
        findById(id);
    }
}
