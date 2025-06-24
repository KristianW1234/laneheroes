package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HeroService {
    ResponseWrapper<Hero> addHero(Hero hero, MultipartFile imgFile);

    ResponseWrapper<Hero> updateHero(Hero hero, MultipartFile imgFile);

    ResponseWrapper<Hero> deleteHero(Long id);

    ResponseWrapper<List<Hero>> getAllHeroes();

    ResponseWrapper<Hero> getHeroById(Long id);

    ResponseWrapper<PagedResponse<Hero>> searchHeroes(String name, String title, Gender gender, String alternateName, Long gameId, Pageable pageable);

    ResponseWrapper<UploadResult> uploadHeroesFromExcel(String excelFile);

    ResponseWrapper<Long> getHeroCount();

    void uploadInitHeroesFromJSON() throws IOException;
}
