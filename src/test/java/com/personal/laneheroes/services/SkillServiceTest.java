package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.SkillJsonDTO;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.*;
import com.personal.laneheroes.enums.SkillType;
import com.personal.laneheroes.repositories.*;
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
public class SkillServiceTest {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    private HeroService heroService;

    @Autowired
    private HeroRepository heroRepository;

    @BeforeEach
    void resetMocks() {
        reset(skillRepository, heroRepository);
    }

    Skill setupSkill(Hero hero, SkillType skillType){
        Skill skill = new Skill();
        skill.setId(1L);
        skill.setSkillName("This Skill Exists");
        skill.setSkillDescription("Description");
        skill.setSkillSlot(0);
        skill.setIsPassive("Y");
        skill.setIsUltimate("N");
        List<SkillType> types = new ArrayList<>();
        types.add(skillType);
        skill.setSkillTypes(types);
        skill.setHero(hero);
        return skill;
    }

    SkillJsonDTO setupSkillJson(Hero hero, String skillType){
        SkillJsonDTO skill = new SkillJsonDTO();
        skill.setId("1");
        skill.setSkillName("This Skill Exists");
        skill.setSkillDescription("Description");
        skill.setSkillSlot(0);
        skill.setIsPassive("Y");
        skill.setIsUltimate("N");
        skill.setSkillTypes(skillType);
        skill.setHeroCode(hero.getHeroCode());
        skill.setHeroId(Long.toString(hero.getId()));
        return skill;
    }

    Hero setupHero(String code){
        Hero hero = new Hero();
        hero.setId(1L);
        hero.setHeroName("This Hero");
        hero.setHeroCode("EG_"+code);
        Game game = new Game();
        game.setId(1L);
        game.setGameCode(code);
        hero.setGame(game);
        return hero;
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
     * ADD SKILL TESTS
     *
     * */

    @Test
    void addSkill_test_1(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_2(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.empty());
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_3(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_4(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("dota");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "INNATE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_5(){
        MockMultipartFile mockFile = setupFile("skill-icon.doc", "fake image content".getBytes());
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_6(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", new byte[0]);
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, mockFile);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addSkill_test_7(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", new byte[0]);
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.addSkill(skillJson, null);
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE SKILL TESTS
     *
     * */

    @Test
    void updateSkill_test_1(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_2(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_3(){
        MockMultipartFile mockFile = setupFile("skill-icon.png", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.empty());
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_4(){
        MockMultipartFile mockFile = setupFile("skill-icon.doc", "fake image content".getBytes());
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");
        skill.setSkillName(null);
        skill.setSkillDescription(null);
        skill.setIsPassive(null);
        skill.setIsUltimate(null);
        skill.setSkillSlot(null);
        skill.setSkillTypes(null);

        skillJson.setSkillName(null);
        skillJson.setSkillDescription(null);
        skillJson.setIsPassive(null);
        skillJson.setIsUltimate(null);
        skillJson.setSkillSlot(null);
        skillJson.setSkillTypes(null);


        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_5(){
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        skill.setSkillName(null);
        skill.setSkillDescription(null);
        skill.setIsPassive(null);
        skill.setIsUltimate(null);
        skill.setSkillSlot(null);
        skill.setSkillTypes(null);

        skillJson.setSkillName(null);
        skillJson.setSkillDescription(null);
        skillJson.setIsPassive(null);
        skillJson.setIsUltimate(null);
        skillJson.setSkillSlot(null);
        skillJson.setSkillTypes(null);


        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_6(){
        Hero hero = setupHero("TG");
        Skill skill = setupSkill(hero, SkillType.DAMAGE);
        SkillJsonDTO skillJson = setupSkillJson(hero, "DAMAGE");

        skill.setSkillName(null);
        skill.setSkillDescription(null);
        skill.setIsPassive(null);
        skill.setIsUltimate(null);
        skill.setSkillSlot(null);
        skill.setSkillTypes(null);

        skillJson.setSkillName(null);
        skillJson.setSkillDescription(null);
        skillJson.setIsPassive(null);
        skillJson.setIsUltimate(null);
        skillJson.setSkillSlot(null);
        skillJson.setSkillTypes(null);
        skillJson.setHeroId(null);


        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, null);
        verify(skillRepository).findById(any());
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateSkill_test_7(){
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Hero hero = setupHero("dota");

        Skill skill = setupSkill(hero, SkillType.INNATE);

        SkillJsonDTO skillJson = setupSkillJson(hero, "INNATE");

        skill.setSkillName(null);
        skill.setSkillDescription(null);
        skill.setIsPassive(null);
        skill.setIsUltimate(null);
        skill.setSkillSlot(null);

        skillJson.setSkillName(null);
        skillJson.setSkillDescription(null);
        skillJson.setIsPassive(null);
        skillJson.setIsUltimate(null);
        skillJson.setSkillSlot(null);


        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(heroRepository.findById(any())).thenReturn(Optional.of(hero));
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        ResponseWrapper<Skill> trial = skillService.updateSkill(skillJson, mockFile);
        verify(skillRepository).findById(any());
        verify(skillRepository).save(any(Skill.class));
        verify(heroRepository).findByHeroCodeIgnoreCase(any());
        verify(heroRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE GAME TESTS
     *
     * */

    @Test
    void deleteSkill_test_1(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);

        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));

        ResponseWrapper<Skill> trial = skillService.deleteSkill(any());
        verify(skillRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void deleteSkill_test_2(){
        when(skillRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Skill> trial = skillService.deleteSkill(any());
        verify(skillRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * GET ALL SKILLS TESTS
     *
     * */

    @Test
    void getAllSkills_test_1(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        Skill skill2 = setupSkill(hero, SkillType.DAMAGE);


        List<Skill> skills = new ArrayList();
        skills.add(skill);
        skills.add(skill2);

        when(skillRepository.findAll()).thenReturn(skills);

        ResponseWrapper<List<SkillJsonDTO>> trial = skillService.getAllSkills();

        verify(skillRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getAllSkills_test_2(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);

        List<Skill> skills = new ArrayList();
        skills.add(skill);

        when(skillRepository.findAll()).thenReturn(skills);

        ResponseWrapper<List<SkillJsonDTO>> trial = skillService.getAllSkills();

        verify(skillRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getAllSkills_test_3(){
        List<Skill> skills = new ArrayList();

        when(skillRepository.findAll()).thenReturn(skills);

        ResponseWrapper<List<SkillJsonDTO>> trial = skillService.getAllSkills();

        verify(skillRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * GET SKILL BY ID TESTS
     *
     * */

    @Test
    void getSkillById_test_1(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);

        when(skillRepository.findById(any())).thenReturn(Optional.of(skill));
        ResponseWrapper<SkillJsonDTO> trial = skillService.getSkillById(skill.getId());
        verify(skillRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getSkillById_test_2(){

        when(skillRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<SkillJsonDTO> trial = skillService.getSkillById(any());
        verify(skillRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * SEARCH SKILLS TESTS
     *
     * */

    @Test
    void searchSkills_test_1(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);
        Skill skill2 = setupSkill(hero, SkillType.DAMAGE);

        List<Skill> skills = new ArrayList();
        skills.add(skill);
        skills.add(skill2);

        Page<Skill> skillPage = new PageImpl<>(skills, PageRequest.of(0,10), 1);

        when(skillRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(skillPage);

        ResponseWrapper<PagedResponse<SkillJsonDTO>> trial
                = skillService.searchSkills("skill-name", null, PageRequest.of(0, 10));

        verify(skillRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchSkills_test_2(){
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);

        List<Skill> skills = new ArrayList();
        skills.add(skill);

        Page<Skill> skillPage = new PageImpl<>(skills, PageRequest.of(0,10), 1);

        when(skillRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(skillPage);

        ResponseWrapper<PagedResponse<SkillJsonDTO>> trial
                = skillService.searchSkills("skill-name", null,  PageRequest.of(0, 10));

        verify(skillRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchSkills_test_3(){

        List<Skill> skills = new ArrayList();

        Page<Skill> skillPage = new PageImpl<>(skills, PageRequest.of(0,10), 0);

        when(skillRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(skillPage);

        ResponseWrapper<PagedResponse<SkillJsonDTO>> trial
                = skillService.searchSkills("skill-name", null, PageRequest.of(0, 10));

        verify(skillRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD SKILLS FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadSkillsFromExcel_test_1() throws URISyntaxException {
        Hero hero = setupHero("EG");
        Skill skill = setupSkill(hero, SkillType.INNATE);

        List<Skill> skills = new ArrayList();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-skills.xlsx").toURI());
        when(skillRepository.findAll()).thenReturn(skills);
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        ResponseWrapper<UploadResult> response = skillService.uploadSkillsFromExcel(path.toString());

        verify(skillRepository).findAll();
        verify(skillRepository, times(10)).save(any(Skill.class));
        verify(heroRepository, times(10)).findByHeroCodeIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadSkillsFromExcel_test_2() throws URISyntaxException {
        Hero hero = setupHero("EG");

        Skill skill = setupSkill(hero, SkillType.INNATE);
        skill.setSkillName("Skill-A-1");


        List<Skill> skills = new ArrayList();
        skills.add(skill);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-skills.xlsx").toURI());
        when(skillRepository.findAll()).thenReturn(skills);
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        ResponseWrapper<UploadResult> response = skillService.uploadSkillsFromExcel(path.toString());

        verify(skillRepository).findAll();
        verify(skillRepository, times(9)).save(any(Skill.class));
        verify(heroRepository, times(10)).findByHeroCodeIgnoreCase(any());
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadSkillsFromExcel_test_3() throws URISyntaxException {
        Hero hero = setupHero("EG");

        Skill skill = setupSkill(hero, SkillType.INNATE);
        //skill.setSkillCode("GE");

        List<Skill> skills = new ArrayList();
        skills.add(skill);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-skills.xlsx").toURI());
        when(skillRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = skillService.uploadSkillsFromExcel(path.toString());

        verify(skillRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT SKILLS FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitSkillsFromJSON_test_1() throws IOException {
        Hero hero = setupHero("EG");

        List<Skill> skills = new ArrayList();

        String path = "data/test-initSkills.json";

        when(skillRepository.count()).thenReturn(0L);
        when(heroRepository.findByHeroCodeIgnoreCase(any())).thenReturn(Optional.of(hero));
        when(skillRepository.saveAll(any())).thenReturn(skills);

        skillService.uploadInitSkillsFromJSON(path.toString());

        verify(skillRepository).count();
        verify(skillRepository).saveAll(any());
        verify(heroRepository, times(9)).findByHeroCodeIgnoreCase(any());
    }

    @Test
    void uploadInitSkillsFromJSON_test_2() throws IOException {

        String path = "data/test-initSkills.json";

        when(skillRepository.count()).thenReturn(1L);

        skillService.uploadInitSkillsFromJSON(path.toString());

        verify(skillRepository).count();
    }

}
