package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.GameJsonDTO;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.exception.EntityNotFoundException;
import com.personal.laneheroes.repositories.CallsignRepository;
import com.personal.laneheroes.repositories.CompanyRepository;
import com.personal.laneheroes.repositories.GameRepository;
import com.personal.laneheroes.repositories.PlatformRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.GameService;
import com.personal.laneheroes.specifications.GameSpecification;
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
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final CallsignRepository callsignRepository;

    private final PlatformRepository platformRepository;

    private final CompanyRepository companyRepository;

    private final ObjectMapper objectMapper;

    @Value("${image-dir}")
    private String imageDir;

    @Override
    public ResponseWrapper<Game> addGame(Game game, MultipartFile imgFile) {
        Game dbGame = new Game();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];

        if (!assignRelatedEntities(game, dbGame, ResponseMessages.ADD_FAIL, responseHolder)) {
            return (ResponseWrapper<Game>) responseHolder[0];
        }

        dbGame.setGameName(game.getGameName());
        dbGame.setGameCode(game.getGameCode());

        if (imgFile != null && !imgFile.isEmpty()){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "game");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbGame.setImgIcon(uploadResult.getData());
            }

        }
        gameRepository.save(dbGame);
        return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                + ResponseMessages.ADD_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbGame);
    }

    @Override
    public ResponseWrapper<Game> updateGame(Game game, MultipartFile imgFile){
        Optional<Game> gamePresence = gameRepository.findById(game.getId());
        if (gamePresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
        Game dbGame  = gamePresence.get();
        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];




        if (!assignRelatedEntities(game, dbGame, ResponseMessages.UPDATE_FAIL, responseHolder)) {
            return (ResponseWrapper<Game>) responseHolder[0];
        }

        if (game.getGameName() != null){
            dbGame.setGameName(game.getGameName());
        }

        if (game.getGameCode() != null){
            dbGame.setGameCode(game.getGameCode());
        }





        if (imgFile != null && !imgFile.isEmpty()){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "game");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbGame.setImgIcon(uploadResult.getData());
            }

        }
        gameRepository.save(dbGame);
        return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                + ResponseMessages.UPDATE_SUCCESS,
                ResponseMessages.SUCCESS_STATUS, dbGame);
    }

    @Override
    public ResponseWrapper<Game> deleteGame(Long id) {
        Optional<Game> gamePresence = gameRepository.findById(id);
        if (gamePresence.isPresent()){
            Game dbGame = gamePresence.get();
            gameRepository.delete(dbGame);
            return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbGame);
        } else {
            return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }


    @Override
    public ResponseWrapper<List<Game>> getAllGames() {
        List<Game> list = gameRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.GAME_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.GAME_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Game> getGameById(Long id) {
        Optional<Game> gamePresence = gameRepository.findById(id);
        return gamePresence.map(
                        game -> new ResponseWrapper<>(ResponseMessages.GAME_SINGLE
                                + " " + ResponseMessages.FOUND,
                                ResponseMessages.SUCCESS_STATUS, game))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.GAME_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<PagedResponse<Game>> searchGames(String name, String code, Long companyId, Long platformId, Long callsignId, Pageable pageable) {
        Specification<Game> spec = GameSpecification.withFilters(name, code, companyId, platformId, callsignId);
        Page<Game> resultPage = gameRepository.findAll(spec, pageable);
        PagedResponse<Game> pagedResponse = new PagedResponse<>(resultPage);

        if (resultPage.hasContent()) {
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getNumberOfElements() + " ";
            if (resultPage.getNumberOfElements() > 1) {
                successMessage += ResponseMessages.GAME_PLURAL.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.GAME_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.GAME_SINGLE.toLowerCase() + " out of " + resultPage.getTotalElements() + " " + ResponseMessages.GAME_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        }
    }

    @Override
    public ResponseWrapper<UploadResult> uploadGamesFromExcel(String excelFile) {
        int totalAdded = 0;
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            List<Game> games = gameRepository.findAll();
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip header

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Game game = mapRowToGame(row);

                if (!gameCopyCheck(games, game.getGameName())) {
                    totalAdded++;
                    gameRepository.save(game);
                }
            }
        } catch (Exception ex) {
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL, ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }

        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS, ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    @Override
    public void uploadInitGamesFromJSON(String path) throws IOException {
        if (gameRepository.count() > 0) return;

        //InputStream inputStream = new FileInputStream(path);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);


        List<GameJsonDTO> gameDTOs = objectMapper.readValue(inputStream, new TypeReference<>() {});

        List<Game> games = new ArrayList<>();

        for (GameJsonDTO dto : gameDTOs) {
            Company company = companyRepository.findByCompanyNameIgnoreCase(dto.company)
                    .orElseThrow(() -> new RuntimeException("Company not found: " + dto.company));

            Callsign callsign = callsignRepository.findByCallsignIgnoreCase(dto.callsign)
                    .orElseThrow(() -> new RuntimeException("Callsign not found: " + dto.callsign));

            Platform platform = platformRepository.findByPlatformNameIgnoreCase(dto.platform)
                    .orElseThrow(() -> new RuntimeException("Platform not found: " + dto.platform));

            Game game = new Game();
            game.setGameCode(dto.gameCode);
            game.setGameName(dto.gameName);
            game.setImgIcon(dto.imgIcon);
            game.setCompany(company);
            game.setCallsign(callsign);
            game.setPlatform(platform);


            games.add(game);
        }

        gameRepository.saveAll(games);

    }

    private boolean gameCopyCheck(List<Game> games, String name) {
        for (Game game : games) {
            if (game.getGameName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean assignRelatedEntities(Game source, Game target, String failMsg, ResponseWrapper<?>[] responseHolder) {
        if (source.getCallsign() != null) {
            Optional<Callsign> callsign = Utility.getValidEntityById(
                    callsignRepository, source.getCallsign().getId(),
                    ResponseMessages.CALLSIGN_SINGLE, failMsg, responseHolder);
            if (callsign.isEmpty()) return false;
            target.setCallsign(callsign.get());
        }

        if (source.getPlatform() != null) {
            Optional<Platform> platform = Utility.getValidEntityById(
                    platformRepository, source.getPlatform().getId(),
                    ResponseMessages.PLATFORM_SINGLE, failMsg, responseHolder);
            if (platform.isEmpty()) return false;
            target.setPlatform(platform.get());
        }

        if (source.getCompany() != null) {
            Optional<Company> company = Utility.getValidEntityById(
                    companyRepository, source.getCompany().getId(),
                    ResponseMessages.COMPANY_SINGLE, failMsg, responseHolder);
            if (company.isEmpty()) return false;
            target.setCompany(company.get());
        }

        return true;
    }

    private Game mapRowToGame(Row row) {
        Game game = new Game();
        game.setImgIcon("noimage.png");

        for (Cell cell : row) {
            int index = cell.getColumnIndex();
            String value = cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : "";

            switch (index) {
                case 0 -> game.setGameName(value);
                case 1 -> game.setCallsign(fetchCallsign(value));
                case 2 -> game.setCompany(fetchCompany(value));
                case 3 -> game.setPlatform(fetchPlatform(value));
                case 4 -> game.setGameCode(value);
                case 5 -> game.setImgIcon(value);
                default -> {
                    //Nothing
                }
            }
        }
        return game;
    }

    private Callsign fetchCallsign(String name) {
        return callsignRepository.findByCallsignIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Callsign", name));
    }

    private Company fetchCompany(String name) {
        return companyRepository.findByCompanyNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Company", name));
    }

    private Platform fetchPlatform(String name) {
        return platformRepository.findByPlatformNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Platform", name));
    }
}
