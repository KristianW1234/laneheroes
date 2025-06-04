package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.repositories.*;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.HeroService;
import com.personal.laneheroes.specifications.HeroSpecification;
import com.personal.laneheroes.utilities.ResponseMessages;
import com.personal.laneheroes.utilities.Utility;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service("HeroServiceImpl")
@Transactional
public class HeroServiceImpl implements HeroService {

    @Autowired
    HeroRepository heroRepository;

    @Autowired
    GameRepository gameRepository;


    @Value("${image-dir}")
    private String imageDir;

    @Override
    @SuppressWarnings("unchecked")
    public ResponseWrapper<Hero> addOrUpdateHero(Hero hero, MultipartFile imgFile, boolean isUpdate) {
        Hero dbHero = new Hero();
        Game dbGame = new Game();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate) {

            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;

            Optional<Hero> heroPresence = heroRepository.findById(hero.getId());
            if (heroPresence.isEmpty()){
                return new ResponseWrapper<>(ResponseMessages.CALLSIGN_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbHero = heroPresence.get();
        }


        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        // Validate Game
        if (!isUpdate || hero.getGame() != null){
            Optional<Game> game = Utility.getValidEntityById(
                    gameRepository,
                    hero.getGame().getId(),
                    ResponseMessages.GAME_SINGLE,
                    failMsg,
                    responseHolder
            );
            if (game.isEmpty()) return (ResponseWrapper<Hero>) responseHolder[0];

            dbGame = game.get();
            dbHero.setGame(dbGame);

        }


        if (!isUpdate || hero.getHeroName() != null){
            dbHero.setHeroName(hero.getHeroName());
            dbHero.setHeroCode(setupHeroCode(hero.getHeroName(), dbGame));
        }

        if (!isUpdate || hero.getHeroTitle() != null){
            dbHero.setHeroTitle(hero.getHeroTitle());
        }

        if (!isUpdate || hero.getAlternateName() != null){
            dbHero.setAlternateName(hero.getAlternateName());
        }



        if (!isUpdate || (imgFile != null && !imgFile.isEmpty())){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "hero");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbHero.setImgIcon(uploadResult.getData());
            }

        }
        heroRepository.save(dbHero);
        return new ResponseWrapper<>(ResponseMessages.HERO_SINGLE + " "
                + successMsg,
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
        try {
            Iterable<Hero> heroes = heroRepository.findAll();
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            Hero hero;

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                hero = new Hero();
                hero.setImgIcon("nophoto.jpg");
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            hero.setHeroCode(nextCell.getStringCellValue());
                            break;
                        case 1:
                            hero.setHeroName(nextCell.getStringCellValue());
                            break;
                        case 2:
                            hero.setHeroTitle(nextCell.getStringCellValue());
                            break;
                        case 3:
                            hero.setHeroGender(Gender.valueOf(nextCell.getStringCellValue()));
                            break;
                        case 4:
                            Optional<Game> game = gameRepository.findByGameNameIgnoreCase(nextCell.getStringCellValue());
                            if (game.isPresent()){
                                hero.setGame(game.get());
                            } else {
                                throw new RuntimeException("Game not found");
                            }
                            break;
                        case 5:
                            hero.setAlternateName(nextCell.getStringCellValue());
                            break;
                        case 6:
                            hero.setImgIcon(nextCell.getStringCellValue());
                            break;
                    }
                }
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
        ;
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


}
