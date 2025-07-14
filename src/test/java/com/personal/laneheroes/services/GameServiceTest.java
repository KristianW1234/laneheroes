package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.repositories.CallsignRepository;
import com.personal.laneheroes.repositories.CompanyRepository;
import com.personal.laneheroes.repositories.GameRepository;
import com.personal.laneheroes.repositories.PlatformRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.persistence.QueryTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
@Import(TestMockConfig.class)
public class GameServiceTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CallsignRepository callsignRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void resetMocks() {
        reset(gameRepository, companyRepository, callsignRepository, platformRepository);
    }

    Game setupGame(Company company, Callsign callsign, Platform platform){
        Game game = new Game();
        game.setId(1L);
        game.setGameName("This Game Exists");
        game.setGameCode("EG");
        game.setCompany(company);
        game.setCallsign(callsign);
        game.setPlatform(platform);
        return game;
    }

    Company setupCompany(){
        Company company = new Company();
        company.setId(1L);
        company.setCompanyName("This Company");
        return company;
    }

    Platform setupPlatform(){
        Platform platform = new Platform();
        platform.setId(1L);
        platform.setPlatformName("This Platform");
        return platform;
    }

    Callsign setupCallsign(){
        Callsign callsign = new Callsign();
        callsign.setId(1L);
        callsign.setCallsign("Hero");
        callsign.setCallsignPlural("Heroes");
        return callsign;
    }

    MockMultipartFile setupFile(String name, byte[] content){
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                name,
                "image/png",
                content
        );
        return mockFile;
    }

    /*
     *
     * ADD GAME TESTS
     *
     * */

    @Test
    void addGame_test_1(){
        MockMultipartFile mockFile = setupFile("game-icon.png", "fake image content".getBytes());
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.addGame(game, mockFile);
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_2(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(callsignRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.addGame(game, null);
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_3(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setCallsign(null);

        when(platformRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.addGame(game, null);
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_4(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setCallsign(null);
        game.setPlatform(null);

        when(companyRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.addGame(game, null);
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_5(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setCompany(null);

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.addGame(game, null);
        verify(gameRepository).save(any(Game.class));
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_6(){
        MockMultipartFile mockFile = setupFile("game-icon.doc", "fake image content".getBytes());
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.addGame(game, mockFile);
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addGame_test_7(){
        MockMultipartFile mockFile = setupFile("game-icon.png", new byte[0]);
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.addGame(game, mockFile);
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE GAME TESTS
     *
     * */

    @Test
    void updateGame_test_1(){
        MockMultipartFile mockFile = setupFile("game-icon.png", "fake image content".getBytes());
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.updateGame(game, mockFile);
        verify(gameRepository).findById(any());
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateGame_test_2(){
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setGameName(null);
        game.setGameCode(null);



        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.updateGame(game, mockFile);
        verify(gameRepository).findById(any());
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateGame_test_3(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setGameName(null);
        game.setGameCode(null);



        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.updateGame(game, null);
        verify(gameRepository).findById(any());
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateGame_test_4(){
        MockMultipartFile mockFile = setupFile("game-icon.doc", "fake image content".getBytes());
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setGameName(null);
        game.setGameCode(null);



        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Game> trial = gameService.updateGame(game, mockFile);
        verify(gameRepository).findById(any());
        verify(gameRepository).save(any(Game.class));
        verify(companyRepository).findById(any());
        verify(callsignRepository).findById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateGame_test_5(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);



        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        when(callsignRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.updateGame(game, null);
        verify(gameRepository).findById(any());
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateGame_test_6(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);


        when(gameRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.updateGame(game, null);
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE GAME TESTS
     *
     * */

    @Test
    void deleteGame_test_1(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        ResponseWrapper<Game> trial = gameService.deleteGame(any());
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void deleteGame_test_2(){
        when(gameRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Game> trial = gameService.deleteGame(any());
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * GET ALL GAMES TESTS
     *
     * */

    @Test
    void getAllGames_test_1(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        Game game2 = setupGame(company, callsign, platform);

        List<Game> games = new ArrayList();
        games.add(game);
        games.add(game2);

        when(gameRepository.findAll()).thenReturn(games);

        ResponseWrapper<List<Game>> trial = gameService.getAllGames();

        verify(gameRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getAllGames_test_2(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        List<Game> games = new ArrayList();
        games.add(game);

        when(gameRepository.findAll()).thenReturn(games);

        ResponseWrapper<List<Game>> trial = gameService.getAllGames();

        verify(gameRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getAllGames_test_3(){
        List<Game> games = new ArrayList();

        when(gameRepository.findAll()).thenReturn(games);

        ResponseWrapper<List<Game>> trial = gameService.getAllGames();

        verify(gameRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * GET GAME BY ID TESTS
     *
     * */

    @Test
    void getGameById_test_1(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        ResponseWrapper<Game> trial = gameService.getGameById(game.getId());
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getGameById_test_2(){

        when(gameRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Game> trial = gameService.getGameById(any());
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * SEARCH GAMES TESTS
     *
     * */

    @Test
    void searchGames_test_1(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        Game game2 = setupGame(company, callsign, platform);

        List<Game> games = new ArrayList();
        games.add(game);
        games.add(game2);

        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(0,10), 1);

        when(gameRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(gamePage);

        ResponseWrapper<PagedResponse<Game>> trial
                = gameService.searchGames("game-name", null, null, null, null, PageRequest.of(0, 10));

        verify(gameRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchGames_test_2(){
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        List<Game> games = new ArrayList();
        games.add(game);

        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(0,10), 1);

        when(gameRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(gamePage);

        ResponseWrapper<PagedResponse<Game>> trial
                = gameService.searchGames("game-name", null, null, null, null, PageRequest.of(0, 10));

        verify(gameRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchGames_test_3(){

        List<Game> games = new ArrayList();

        Page<Game> gamePage = new PageImpl<>(games, PageRequest.of(0,10), 0);

        when(gameRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(gamePage);

        ResponseWrapper<PagedResponse<Game>> trial
                = gameService.searchGames("game-name", null, null, null, null, PageRequest.of(0, 10));

        verify(gameRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD GAMES FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadGamesFromExcel_test_1() throws URISyntaxException {
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);

        List<Game> games = new ArrayList();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-games.xlsx").toURI());
        when(gameRepository.findAll()).thenReturn(games);
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findByCallsignIgnoreCase(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findByPlatformNameIgnoreCase(any())).thenReturn(Optional.of(platform));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        ResponseWrapper<UploadResult> response = gameService.uploadGamesFromExcel(path.toString());

        verify(gameRepository).findAll();
        verify(gameRepository, times(2)).save(any(Game.class));
        verify(companyRepository, times(2)).findByCompanyNameIgnoreCase(any());
        verify(callsignRepository, times(2)).findByCallsignIgnoreCase(any());
        verify(platformRepository, times(2)).findByPlatformNameIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadGamesFromExcel_test_2() throws URISyntaxException {
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setGameCode("GE");

        List<Game> games = new ArrayList();
        games.add(game);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-games.xlsx").toURI());
        when(gameRepository.findAll()).thenReturn(games);
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findByCallsignIgnoreCase(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findByPlatformNameIgnoreCase(any())).thenReturn(Optional.of(platform));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        ResponseWrapper<UploadResult> response = gameService.uploadGamesFromExcel(path.toString());

        verify(gameRepository).findAll();
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(companyRepository, times(2)).findByCompanyNameIgnoreCase(any());
        verify(callsignRepository, times(2)).findByCallsignIgnoreCase(any());
        verify(platformRepository, times(2)).findByPlatformNameIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadGamesFromExcel_test_3() throws URISyntaxException {
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();
        Game game = setupGame(company, callsign, platform);
        game.setGameCode("GE");

        List<Game> games = new ArrayList();
        games.add(game);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-games.xlsx").toURI());
        when(gameRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = gameService.uploadGamesFromExcel(path.toString());

        verify(gameRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT GAMES FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitGamesFromJSON_test_1() throws IOException {
        Company company = setupCompany();
        Callsign callsign = setupCallsign();
        Platform platform = setupPlatform();

        List<Game> games = new ArrayList();

        String path = "data/test-initGames.json";

        when(gameRepository.count()).thenReturn(0L);
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.of(company));
        when(callsignRepository.findByCallsignIgnoreCase(any())).thenReturn(Optional.of(callsign));
        when(platformRepository.findByPlatformNameIgnoreCase(any())).thenReturn(Optional.of(platform));
        when(gameRepository.saveAll(any())).thenReturn(games);

        gameService.uploadInitGamesFromJSON(path.toString());

        verify(gameRepository).count();
        verify(gameRepository).saveAll(any());
        verify(companyRepository, times(2)).findByCompanyNameIgnoreCase(any());
        verify(callsignRepository, times(2)).findByCallsignIgnoreCase(any());
        verify(platformRepository, times(2)).findByPlatformNameIgnoreCase(any());
    }

    @Test
    void uploadInitGamesFromJSON_test_2() throws IOException {

        String path = "data/test-initGames.json";

        when(gameRepository.count()).thenReturn(1L);

        gameService.uploadInitGamesFromJSON(path.toString());

        verify(gameRepository).count();
    }

}
