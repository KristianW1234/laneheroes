package com.personal.laneheroes.services;


import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Game;
import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.enums.Gender;
import com.personal.laneheroes.repositories.GameRepository;
import com.personal.laneheroes.repositories.HeroRepository;
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


@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
@Import(TestMockConfig.class)
class HeroServiceTest {



    @Autowired
    private HeroService heroService;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private GameRepository gameRepository;

    @BeforeEach
    void resetMocks() {
        reset(heroRepository, gameRepository);
    }

    Hero setupHero(Game game){
        Hero hero = new Hero();
        hero.setHeroName("HeroName");
        hero.setHeroTitle("HeroTitle");
        hero.setHeroGender(Gender.MALE);
        hero.setAlternateName("");
        hero.setDisplayByTitle("N");
        hero.setHeroDescription("Description");
        hero.setHeroLore("Lore");
        hero.setGame(game);

        return hero;
    }

    Game setupGame(){
        Game game = new Game();
        game.setId(1L);
        game.setGameName("Existing Game");
        return game;
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
    * ADD HERO TESTS
    *
    * */

    @Test
    void addHero_test_1() {
        MockMultipartFile mockFile = setupFile("hero-icon.png", "fake image content".getBytes());
        Game game = setupGame();
        Hero hero = setupHero(game);
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        ResponseWrapper<Hero> trial = heroService.addHero(hero, mockFile);
        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addHero_test_2() {
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setGame(null);
        hero.setHeroTitle(null);
        hero.setAlternateName(null);
        hero.setHeroDescription(null);
        hero.setHeroLore(null);

        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));


        ResponseWrapper<Hero> trial = heroService.addHero(hero, mockFile);

        verify(heroRepository).save(any(Hero.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void addHero_test_3() {
        MockMultipartFile mockFile = setupFile("hero-icon.png", "fake image content".getBytes());

        Game game = setupGame();
        Hero hero = setupHero(game);


        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.empty());


        ResponseWrapper<Hero> trial = heroService.addHero(hero, mockFile);

        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    @Test
    void addHero_test_4() {
        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setHeroName("The HeroName");


        ResponseWrapper<Hero> expected = new ResponseWrapper<>();
        expected.setData(hero);
        expected.setMessage(ResponseMessages.HERO_SINGLE + " " + ResponseMessages.ADD_SUCCESS);
        expected.setStatus(ResponseMessages.SUCCESS_STATUS);

        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        ResponseWrapper<Hero> trial = heroService.addHero(hero, null);

        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void addHero_test_5() {

        MockMultipartFile mockFile = setupFile("hero-icon.doc", "fake image content".getBytes());

        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setHeroName("Hero & Baby");

        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));


        ResponseWrapper<Hero> trial = heroService.addHero(hero, mockFile);

        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * UPDATE HERO TESTS
     *
     * */

    @Test
    void updateHero_test_1() {
        MockMultipartFile mockFile = setupFile("hero-icon.png", "fake image content".getBytes());
        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));
        ResponseWrapper<Hero> trial = heroService.updateHero(hero, mockFile);

        verify(heroRepository).findById(any());
        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateHero_test_2() {
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setHeroName(null);
        hero.setGame(null);
        hero.setHeroTitle(null);
        hero.setAlternateName(null);
        hero.setHeroDescription(null);
        hero.setHeroLore(null);
        hero.setDisplayByTitle(null);
        hero.setHeroGender(null);



        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));


        ResponseWrapper<Hero> trial = heroService.updateHero(hero, mockFile);

        verify(heroRepository).findById(any());
        verify(heroRepository).save(any(Hero.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void updateHero_test_3() {
        MockMultipartFile mockFile = setupFile("hero-icon.png", "fake image content".getBytes());

        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.empty());


        ResponseWrapper<Hero> trial = heroService.updateHero(hero, mockFile);

        verify(heroRepository).findById(any());
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    @Test
    void updateHero_test_4() {
        Game game = setupGame();
        Hero hero = setupHero(game);



        ResponseWrapper<Hero> expected = new ResponseWrapper<>();
        expected.setData(hero);
        expected.setMessage(ResponseMessages.HERO_SINGLE + " " + ResponseMessages.ADD_SUCCESS);
        expected.setStatus(ResponseMessages.SUCCESS_STATUS);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));

        ResponseWrapper<Hero> trial = heroService.updateHero(hero, null);

        verify(heroRepository).findById(any());
        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void updateHero_test_5() {

        MockMultipartFile mockFile = setupFile("hero-icon.doc", "fake image content".getBytes());

        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        when(gameRepository.findById(any())).thenReturn(Optional.of(game));


        ResponseWrapper<Hero> trial = heroService.updateHero(hero, mockFile);

        verify(heroRepository).findById(any());
        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void updateHero_test_6() {
        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.empty());


        ResponseWrapper<Hero> trial = heroService.updateHero(hero, null);

        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    /*
     *
     * DELETE HERO TESTS
     *
     * */

    @Test
    void deleteHero_test_1(){
        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));

        ResponseWrapper<Hero> trial = heroService.deleteHero(any());

        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void deleteHero_test_2(){

        when(heroRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Hero> trial = heroService.deleteHero(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    /*
     *
     * GET ALL HEROES TESTS
     *
     * */

    @Test
    void getAllHeroes_test_1(){
        Game game = setupGame();
        Hero hero = setupHero(game);
        Hero hero2 = setupHero(game);

        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);
        heroes.add(hero2);

        when(heroRepository.findAll()).thenReturn(heroes);

        ResponseWrapper<List<Hero>> trial = heroService.getAllHeroes();
        verify(heroRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllHeroes_test_2(){
        Game game = setupGame();
        Hero hero = setupHero(game);

        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);

        when(heroRepository.findAll()).thenReturn(heroes);

        ResponseWrapper<List<Hero>> trial = heroService.getAllHeroes();
        verify(heroRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllHeroes_test_3(){

        List<Hero> heroes = new ArrayList<>();


        when(heroRepository.findAll()).thenReturn(heroes);

        ResponseWrapper<List<Hero>> trial = heroService.getAllHeroes();
        verify(heroRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * GET HERO BY ID TESTS
     *
     * */

    @Test
    void getHeroById_test_1(){
        Game game = setupGame();
        Hero hero = setupHero(game);

        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));

        ResponseWrapper<Hero> trial = heroService.getHeroById(hero.getId());

        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getHeroById_test_2(){

        when(heroRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Hero> trial = heroService.getHeroById(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    /*
     *
     * SEARCH HEROES TESTS
     *
     * */

    @Test
    void searchHeroes_test_1(){
        Game game = setupGame();
        Hero hero = setupHero(game);
        Hero hero2 = setupHero(game);

        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);
        heroes.add(hero2);

        Page<Hero> heroPage = new PageImpl<>(heroes, PageRequest.of(0,10), 1);

        when(heroRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(heroPage);

        ResponseWrapper<PagedResponse<Hero>> trial = heroService.searchHeroes(
                "hero-name", null, null, null, null, PageRequest.of(0, 10));

        verify(heroRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchHeroes_test_2(){
        Game game = setupGame();
        Hero hero = setupHero(game);

        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);

        Page<Hero> heroPage = new PageImpl<>(heroes, PageRequest.of(0,10), 1);

        when(heroRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(heroPage);

        ResponseWrapper<PagedResponse<Hero>> trial = heroService.searchHeroes(
                "hero-name", null, null, null, null, PageRequest.of(0, 10));

        verify(heroRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchHeroes_test_3(){

        List<Hero> heroes = new ArrayList<>();


        Page<Hero> heroPage = new PageImpl<>(heroes, PageRequest.of(0,10), 0);

        when(heroRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(heroPage);

        ResponseWrapper<PagedResponse<Hero>> trial = heroService.searchHeroes(
                "hero-name", null, null, null, null, PageRequest.of(0, 10));

        verify(heroRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD HEROES FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadHeroesFromExcel_test_1() throws URISyntaxException {
        Game game = setupGame();
        Hero hero = setupHero(game);

        List<Hero> heroes = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-heroes.xlsx").toURI());

        when(heroRepository.findAll()).thenReturn(heroes);
        when(gameRepository.findByGameNameIgnoreCase(any())).thenReturn(Optional.of(game));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        ResponseWrapper<UploadResult> response = heroService.uploadHeroesFromExcel(path.toString());

        verify(heroRepository).findAll();
        verify(heroRepository, times(2)).save(any(Hero.class));
        verify(gameRepository, times(2)).findByGameNameIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadHeroesFromExcel_test_2() throws URISyntaxException {
        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setHeroCode("hero_EG");
        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-heroes.xlsx").toURI());

        when(heroRepository.findAll()).thenReturn(heroes);
        when(gameRepository.findByGameNameIgnoreCase(any())).thenReturn(Optional.of(game));
        when(heroRepository.save(any(Hero.class))).thenReturn(hero);
        ResponseWrapper<UploadResult> response = heroService.uploadHeroesFromExcel(path.toString());

        verify(heroRepository).findAll();
        verify(heroRepository).save(any(Hero.class));
        verify(gameRepository, times(2)).findByGameNameIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadHeroesFromExcel_test_3() throws URISyntaxException {
        Game game = setupGame();
        Hero hero = setupHero(game);
        hero.setHeroCode("hero_EG");
        List<Hero> heroes = new ArrayList<>();

        heroes.add(hero);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-heroes.xlsx").toURI());

        when(heroRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = heroService.uploadHeroesFromExcel(path.toString());

        verify(heroRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT HEROES FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitHeroesFromJSON_test_1() throws URISyntaxException, IOException {
        Game game = setupGame();

        List<Hero> heroes = new ArrayList<>();


        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-initHeroes.json").toURI());

        when(heroRepository.count()).thenReturn(0L);
        when(gameRepository.findByGameNameIgnoreCase(any())).thenReturn(Optional.of(game));
        when(heroRepository.saveAll(any())).thenReturn(heroes);

        heroService.uploadInitHeroesFromJSON(path.toString());

        verify(heroRepository).count();
        verify(heroRepository).saveAll(any());
        verify(gameRepository, times(2)).findByGameNameIgnoreCase(any());

    }

    @Test
    void uploadInitHeroesFromJSON_test_2() throws URISyntaxException, IOException {


        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-initHeroes.json").toURI());

        when(heroRepository.count()).thenReturn(1L);

        heroService.uploadInitHeroesFromJSON(path.toString());

        verify(heroRepository).count();

    }

}
