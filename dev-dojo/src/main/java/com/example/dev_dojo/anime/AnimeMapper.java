package com.example.dev_dojo.anime;

import com.dev_dojo.dto.AnimeGetResponse;
import com.dev_dojo.dto.AnimePostRequest;
import com.dev_dojo.dto.AnimePutRequest;
import com.dev_dojo.dto.PageAnime;
import com.example.dev_dojo.domain.Anime;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

    AnimeMapper MAPPER = Mappers.getMapper(AnimeMapper.class);

    // @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest putRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);



    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);

    PageAnime toPageAnimeGetResponse(Page<Anime> animePaginated);
}
