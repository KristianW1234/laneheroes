package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.HeroService;
import com.personal.laneheroes.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/hero")
public class HeroController {

    @Autowired
    HeroService heroService;

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Hero>>> getAllHeroes() {
        ResponseWrapper<List<Hero>> response = heroService.getAllHeroes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Hero>> getOneHero(@PathVariable Long id) {
        ResponseWrapper<Hero> response = heroService.getHeroById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<ResponseWrapper<Long>> getHeroCount() {
        ResponseWrapper<Long> response = heroService.getHeroCount();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Hero>> addHero(
            @RequestPart("hero") Hero hero,
            @RequestPart("imgFile") MultipartFile imgFile) {

        ResponseWrapper<Hero> result = heroService.addOrUpdateHero(hero, imgFile, false);
        return ResponseEntity.ok(result);
    }

    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Hero>> updateHero(
            @RequestPart("hero") Hero hero,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {

        ResponseWrapper<Hero> result = heroService.addOrUpdateHero(hero, imgFile, true);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Hero>> deleteHero(@PathVariable Long id) {
        ResponseWrapper<Hero> response = heroService.deleteHero(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PagedResponse<Hero>>> searchHeroes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Gender gender,
            @RequestParam(required = false) String alternateName,
            @RequestParam(required = false) Long gameId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        ResponseWrapper<PagedResponse<Hero>> response = heroService.searchHeroes(name, title, gender, alternateName, gameId, Utility.setupPageable(page, size, sortBy, sortOrder));
        return ResponseEntity.ok(response);
    }
}
