package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HeroService {
    ResponseWrapper<Hero> addOrUpdateHero(Hero hero, MultipartFile imgFile, boolean isUpdate);

    ResponseWrapper<Hero> deleteHero(Long id);

    ResponseWrapper<List<Hero>> getAllHeroes();

    ResponseWrapper<Hero> getHeroById(Long id);

    ResponseWrapper<PagedResponse<Hero>> searchHeroes(String name, String title, String alternateName, Long gameId, Pageable pageable);

    ResponseWrapper<UploadResult> uploadHeroesFromExcel(String excelFile);

    ResponseWrapper<Long> getHeroCount();
}
