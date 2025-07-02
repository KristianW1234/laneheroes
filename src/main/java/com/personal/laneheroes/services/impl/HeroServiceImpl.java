package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.HeroJsonDTO;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.exception.EntityNotFoundException;
import com.personal.laneheroes.repositories.*;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.HeroService;
import com.personal.laneheroes.specifications.HeroSpecification;
import com.personal.laneheroes.utilities.ResponseMessages;
import com.personal.laneheroes.utilities.Utility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class HeroServiceImpl implements HeroService {


    private final HeroRepository heroRepository;


    private final GameRepository gameRepository;

    private final ObjectMapper objectMapper;



    @Value("${image-dir}")
    private String imageDir;


    @Override
    public ResponseWrapper<Hero> addHero(Hero hero, MultipartFile imgFile) {
        Hero dbHero = new Hero();
        Game dbGame = new Game();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        // Validate Game
        if (hero.getGame() != null){
            Optional<Game> game = Utility.getValidEntityById(
                    gameRepository,
                    hero.getGame().getId(),
                    ResponseMessages.GAME_SINGLE,
                    ResponseMessages.ADD_FAIL,
                    responseHolder
            );
            if (game.isEmpty()) return (ResponseWrapper<Hero>) responseHolder[0];

            dbGame = game.get();
            dbHero.setGame(dbGame);

        }
        dbHero.setHeroName(hero.getHeroName());
        dbHero.setHeroCode(setupHeroCode(hero.getHeroName(), dbGame));
        if(hero.getHeroTitle() != null){
            dbHero.setHeroTitle(hero.getHeroTitle());
        }


        dbHero.setHeroGender(hero.getHeroGender());
        dbHero.setDisplayByTitle(hero.getDisplayByTitle());
        if (hero.getAlternateName() != null){
            dbHero.setAlternateName(hero.getAlternateName());
        }
        if (hero.getHeroDescription() != null){
            dbHero.setHeroDescription(hero.getHeroDescription());
        }

        if (hero.getHeroLore() != null){
            dbHero.setHeroDescription(hero.getHeroLore());
        }

        if (imgFile != null && !imgFile.isEmpty()){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "hero");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbHero.setImgIcon(uploadResult.getData());
            }

        }
        heroRepository.save(dbHero);
        return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbHero);
    }

    @Override
    public ResponseWrapper<Hero> updateHero(Hero hero, MultipartFile imgFile) {
        Game dbGame = new Game();
        Optional<Hero> heroPresence = heroRepository.findById(hero.getId());
        if (heroPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Hero dbHero = heroPresence.get();


        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        // Validate Game
        if (hero.getGame() != null){
            Optional<Game> game = Utility.getValidEntityById(
                    gameRepository,
                    hero.getGame().getId(),
                    ResponseMessages.GAME_SINGLE,
                    ResponseMessages.UPDATE_FAIL,
                    responseHolder
            );
            if (game.isEmpty()) return (ResponseWrapper<Hero>) responseHolder[0];

            dbGame = game.get();
            dbHero.setGame(dbGame);

        }


        if (hero.getHeroName() != null){
            dbHero.setHeroName(hero.getHeroName());
            dbHero.setHeroCode(setupHeroCode(hero.getHeroName(), dbGame));
        }

        if (hero.getHeroTitle() != null){
            dbHero.setHeroTitle(hero.getHeroTitle());
        }

        if (hero.getHeroGender() != null){
            dbHero.setHeroGender(hero.getHeroGender());
        }

        if (hero.getAlternateName() != null){
            dbHero.setAlternateName(hero.getAlternateName());
        }

        if (hero.getHeroDescription() != null){
            dbHero.setHeroDescription(hero.getHeroDescription());
        }

        if (hero.getHeroLore() != null){
            dbHero.setHeroLore(hero.getHeroLore());
        }

        if (hero.getDisplayByTitle() != null){
            dbHero.setDisplayByTitle(hero.getDisplayByTitle());
        }

        if (imgFile != null && !imgFile.isEmpty()){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "hero");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbHero.setImgIcon(uploadResult.getData());
            }

        }
        heroRepository.save(dbHero);
        return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbHero);
    }

    @Override
    public ResponseWrapper<Hero> deleteHero(Long id) {
        Optional<Hero> heroPresence = heroRepository.findById(id);
        if (heroPresence.isPresent()){
            Hero dbHero = heroPresence.get();
            heroRepository.delete(dbHero);
            return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbHero);
        } else {
            return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }


    @Override
    public ResponseWrapper<List<Hero>> getAllHeroes() {
        List<Hero> list = heroRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.HERO_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.HERO_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Hero> getHeroById(Long id) {
        Optional<Hero> heroPresence = heroRepository.findById(id);
        return heroPresence.map(
                        company -> new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, company))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<PagedResponse<Hero>> searchHeroes(String name, String title, Gender gender, String alternateName, Long gameId, Pageable pageable) {

        Specification<Hero> spec = HeroSpecification.withFilters(name, title, gender, gameId, alternateName);
        Page<Hero> resultPage = heroRepository.findAll(spec, pageable);
        PagedResponse<Hero> pagedResponse = new PagedResponse<>(resultPage);

        if (resultPage.hasContent()) {
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getNumberOfElements() + " ";
            if (resultPage.getNumberOfElements() > 1) {
                successMessage += ResponseMessages.HERO_PLURAL.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.HERO_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.HERO_SINGLE.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.HERO_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        }
    }

    @Override
    public ResponseWrapper<UploadResult> uploadHeroesFromExcel(String excelFile) {
        int totalAdded = 0;
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream);
        ){
            Iterable<Hero> heroes = heroRepository.findAll();
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Hero hero = mapRowToHero(nextRow);
                if (!heroCopyCheck(heroes, hero.getHeroCode())) {
                    totalAdded++;
                    heroRepository.save(hero);

                }

            }

        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL , ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }
        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS , ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    @Override
    public ResponseWrapper<Long> getHeroCount() {
        Long count = heroRepository.count();
        return new ResponseWrapper<>(ResponseMessages.COUNT_SUCCESS , ResponseMessages.SUCCESS_STATUS, count);
    }

    @Override
    public void uploadInitHeroesFromJSON(String path) throws IOException {
        if (heroRepository.count() > 0) return;

        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        InputStream inputStream = new FileInputStream(path);

        List<HeroJsonDTO> heroDTOs = objectMapper.readValue(inputStream, new TypeReference<>() {});

        List<Hero> heroes = new ArrayList<>();

        for (HeroJsonDTO dto : heroDTOs) {
            Game game = gameRepository.findByGameNameIgnoreCase(dto.game)
                    .orElseThrow(() -> new RuntimeException("Game not found: " + dto.game));

            Hero hero = new Hero();
            hero.setHeroCode(dto.heroCode);
            hero.setHeroName(dto.heroName);
            hero.setHeroTitle(dto.heroTitle);
            hero.setHeroGender(dto.heroGender);
            hero.setGame(game);
            hero.setImgIcon(dto.imgIcon);
            hero.setHeroDescription(dto.heroDescription);
            hero.setHeroLore(dto.heroLore);
            hero.setDisplayByTitle(dto.displayByTitle);

            heroes.add(hero);
        }

        heroRepository.saveAll(heroes);

    }

    private boolean heroCopyCheck(Iterable<Hero> heroes, String code) {
        for (Hero hero : heroes) {
            if (hero.getHeroCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    private String setupHeroCode(String heroName, Game game) {
        if (heroName.startsWith("The ")) {
            heroName = heroName.substring(4, heroName.length());
        }

        String[] arr = heroName.split("[ ,&']");
        String part = "";
        StringBuilder nameCode = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].isEmpty() && !arr[i].equalsIgnoreCase("and")) {
                part = arr[i].toUpperCase();
                if (arr[i].length() > 1) {
                    part = arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1);
                }
                nameCode.append(part);
            }

        }
        return nameCode.toString() + "_" + game.getGameCode();
    }

    private Hero mapRowToHero(Row row) {
        Hero hero = new Hero();
        hero.setImgIcon("nophoto.jpg");

        for (Cell cell : row) {
            int index = cell.getColumnIndex();
            String value = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";

            switch (index) {
                case 0 -> hero.setHeroCode(value);
                case 1 -> hero.setHeroName(value);
                case 2 -> hero.setHeroTitle(value);
                case 3 -> hero.setHeroGender(Gender.valueOf(value));
                case 4 -> hero.setGame(fetchGame(value));
                case 5 -> hero.setAlternateName(value);
                case 6 -> hero.setImgIcon(value);
                case 7 -> hero.setHeroDescription(value);
                case 8 -> hero.setHeroLore(value);
                case 9 -> hero.setDisplayByTitle(value);
                default -> {
                    //Nothing
                }
            }
        }
        return hero;
    }

    private Game fetchGame(String name) {
        return gameRepository.findByGameNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Game", name));
    }


}
