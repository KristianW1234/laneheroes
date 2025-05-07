package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.entities.Game;
import com.personal.laneheroes.entities.Platform;
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

@Service("GameServiceImpl")
@Transactional
public class GameServiceImpl implements GameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    CallsignRepository callsignRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Value("${image-dir}")
    private String imageDir;

    @Override
    @SuppressWarnings("unchecked")
    public ResponseWrapper<Game> addOrUpdateGame(Game game, MultipartFile imgFile, boolean isUpdate) {
        Game dbGame = new Game();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate) {

            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;

            Optional<Game> gamePresence = gameRepository.findById(game.getId());
            if (gamePresence.isEmpty()){
                return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbGame = gamePresence.get();
        }


        ResponseWrapper<?>[] responseHolder = new ResponseWrapper<?>[1];




        // Validate Callsign
        if (!isUpdate || game.getCallsign() != null){
            Optional<Callsign> callsign = Utility.getValidEntityById(
                    callsignRepository,
                    game.getCallsign().getId(),
                    ResponseMessages.CALLSIGN_SINGLE,
                    failMsg,
                    responseHolder
            );
            if (callsign.isEmpty()) return (ResponseWrapper<Game>) responseHolder[0];

            dbGame.setCallsign(callsign.get());
        }


        // Validate Platform
        if (!isUpdate || game.getPlatform() != null){
            Optional<Platform> platform = Utility.getValidEntityById(
                    platformRepository,
                    game.getPlatform().getId(),
                    ResponseMessages.PLATFORM_SINGLE,
                    failMsg,
                    responseHolder
            );
            if (platform.isEmpty()) return (ResponseWrapper<Game>) responseHolder[0];

            dbGame.setPlatform(platform.get());
        }


        // Validate Company

        if (!isUpdate || game.getCompany() != null){
            Optional<Company> company = Utility.getValidEntityById(
                    companyRepository,
                    game.getCompany().getId(),
                    ResponseMessages.COMPANY_SINGLE,
                    failMsg,
                    responseHolder
            );
            if (company.isEmpty()) return (ResponseWrapper<Game>) responseHolder[0];

            dbGame.setCompany(company.get());

        }

        if (!isUpdate || game.getGameName() != null){
            dbGame.setGameName(game.getGameName());
        }

        if (!isUpdate || game.getGameCode() != null){
            dbGame.setGameCode(game.getGameCode());
        }





        if (!isUpdate || (imgFile != null && !imgFile.isEmpty())){
            ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "game");
            if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                dbGame.setImgIcon(uploadResult.getData());
            }

        }
        gameRepository.save(dbGame);
        return new ResponseWrapper<>(ResponseMessages.GAME_SINGLE + " "
                + successMsg,
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
        try {
            List<Game> games = gameRepository.findAll();
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            Game game;
            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                game = new Game();
                game.setImgIcon("noimage.png");
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            game.setGameName(nextCell.getStringCellValue());
                            break;
                        case 1:
                            Optional<Callsign> callsign = callsignRepository.findByCallsignIgnoreCase(nextCell.getStringCellValue());
                            if (callsign.isPresent()){
                                game.setCallsign(callsign.get());
                            } else {
                                throw new RuntimeException("Callsign not found");
                            }
                            break;
                        case 2:
                            Optional<Company> company = companyRepository.findByCompanyNameIgnoreCase(nextCell.getStringCellValue());
                            if (company.isPresent()){
                                game.setCompany(company.get());
                            } else {
                                throw new RuntimeException("Company not found");
                            }
                            break;
                        case 3:
                            Optional<Platform> platform = platformRepository.findByPlatformNameIgnoreCase(nextCell.getStringCellValue());
                            if (platform.isPresent()){
                                game.setPlatform(platform.get());
                            } else {
                                throw new RuntimeException("Platform not found");
                            }
                            break;
                        case 4:
                            game.setGameCode(nextCell.getStringCellValue());
                            break;
                    }
                }
                if (!gameCopyCheck(games, game.getGameName())) {
                    totalAdded++;
                    gameRepository.save(game);
                }

            }

        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL , ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }
        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS , ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    private boolean gameCopyCheck(List<Game> games, String name) {
        for (Game game : games) {
            if (game.getGameName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
