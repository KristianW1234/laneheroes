package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.CountDTO;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.repositories.*;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.utilities.ResponseMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableAutoConfiguration(exclude = {
        org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration.class
})
@Import({TestMockConfig.class , AdminServiceTest.ServiceMockConfig.class})
public class AdminServiceTest {

    @TestConfiguration
    static class ServiceMockConfig {
        @Bean
        public CompanyService companyService() {
            return mock(CompanyService.class);
        }

        @Bean
        public CallsignService callsignService() {
            return mock(CallsignService.class);
        }

        @Bean
        public PlatformService platformService() {
            return mock(PlatformService.class);
        }

        @Bean
        public GameService gameService() {
            return mock(GameService.class);
        }

        @Bean
        public HeroService heroService() {
            return mock(HeroService.class);
        }

        @Bean
        public SkillService skillService() {
            return mock(SkillService.class);
        }
    }

    @Autowired
    private AdminService adminService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CallsignService callsignService;

    @Autowired
    private CallsignRepository callsignRepository;

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private HeroService heroService;

    @Autowired
    private HeroRepository heroRepository;

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void resetMocks() {
        reset(heroService, gameService, companyService, callsignService, platformService, skillService, heroRepository, gameRepository, companyRepository, callsignRepository, platformRepository, userRepository, skillRepository);
    }

    @Test
    void uploadAllData_test_1(){
        int success = 2;
        ResponseWrapper<UploadResult> genericResult = new ResponseWrapper<>();
        genericResult.setStatus(ResponseMessages.SUCCESS_STATUS);
        genericResult.setData(UploadResult.success(success));
        when(companyService.uploadCompaniesFromExcel(anyString())).thenReturn(genericResult);
        when(callsignService.uploadCallsignsFromExcel(anyString())).thenReturn(genericResult);
        when(platformService.uploadPlatformsFromExcel(anyString())).thenReturn(genericResult);
        when(gameService.uploadGamesFromExcel(anyString())).thenReturn(genericResult);
        when(heroService.uploadHeroesFromExcel(anyString())).thenReturn(genericResult);
        when(skillService.uploadSkillsFromExcel(anyString())).thenReturn(genericResult);

        String path = "dummy.xlsx";
        String result = adminService.uploadAllData(path,path,path,path,path, path);

        verify(companyService).uploadCompaniesFromExcel(any());
        verify(callsignService).uploadCallsignsFromExcel(any());
        verify(platformService).uploadPlatformsFromExcel(any());
        verify(gameService).uploadGamesFromExcel(any());
        verify(heroService).uploadHeroesFromExcel(any());
        verify(skillService).uploadSkillsFromExcel(any());

        assertNotNull(result);
        assertEquals(success + " companies, "+success+" callsigns, "+success+" platforms, "+success+" games, "+success+" heroes, "+success+" skills saved.", result);

    }

    @Test
    void uploadAllData_test_2(){
        int success = 0;
        ResponseWrapper<UploadResult> genericResult = new ResponseWrapper<>();
        genericResult.setStatus(ResponseMessages.SUCCESS_STATUS);
        genericResult.setData(UploadResult.success(success));
        when(companyService.uploadCompaniesFromExcel(anyString())).thenReturn(genericResult);
        when(callsignService.uploadCallsignsFromExcel(anyString())).thenReturn(genericResult);
        when(platformService.uploadPlatformsFromExcel(anyString())).thenReturn(genericResult);
        when(gameService.uploadGamesFromExcel(anyString())).thenReturn(genericResult);
        when(heroService.uploadHeroesFromExcel(anyString())).thenReturn(genericResult);
        when(skillService.uploadSkillsFromExcel(anyString())).thenReturn(genericResult);

        String path = "dummy.xlsx";
        String result = adminService.uploadAllData(path,path,path,path,path,path);

        verify(companyService).uploadCompaniesFromExcel(any());
        verify(callsignService).uploadCallsignsFromExcel(any());
        verify(platformService).uploadPlatformsFromExcel(any());
        verify(gameService).uploadGamesFromExcel(any());
        verify(heroService).uploadHeroesFromExcel(any());
        verify(skillService).uploadSkillsFromExcel(any());

        assertNotNull(result);
        assertEquals(success + " companies, "+success+" callsigns, "+success+" platforms, "+success+" games, "+success+" heroes, "+success+" skills saved.", result);

    }

    @Test
    void uploadAllData_test_3(){
        int success = 0;
        ResponseWrapper<UploadResult> genericResult = new ResponseWrapper<>();
        genericResult.setStatus(ResponseMessages.FAIL_STATUS);
        genericResult.setData(UploadResult.error("Error Detected"));
        when(companyService.uploadCompaniesFromExcel(anyString())).thenReturn(genericResult);
        when(callsignService.uploadCallsignsFromExcel(anyString())).thenReturn(genericResult);
        when(platformService.uploadPlatformsFromExcel(anyString())).thenReturn(genericResult);
        when(gameService.uploadGamesFromExcel(anyString())).thenReturn(genericResult);
        when(heroService.uploadHeroesFromExcel(anyString())).thenReturn(genericResult);
        when(skillService.uploadSkillsFromExcel(anyString())).thenReturn(genericResult);

        String path = "dummy.xlsx";
        String result = adminService.uploadAllData(path,path,path,path,path,path);

        verify(companyService).uploadCompaniesFromExcel(any());
        verify(callsignService).uploadCallsignsFromExcel(any());
        verify(platformService).uploadPlatformsFromExcel(any());
        verify(gameService).uploadGamesFromExcel(any());
        verify(heroService).uploadHeroesFromExcel(any());
        verify(skillService).uploadSkillsFromExcel(any());

        assertNotNull(result);
        assertEquals(success + " companies, "+success+" callsigns, "+success+" platforms, "+success+" games, "+success+" heroes, "+success+" skills saved.", result);

    }

    @Test
    void getAllCounts_test(){
        long countRes = 1L;
        when(companyRepository.count()).thenReturn(countRes);
        when(callsignRepository.count()).thenReturn(countRes);
        when(platformRepository.count()).thenReturn(countRes);
        when(gameRepository.count()).thenReturn(countRes);
        when(heroRepository.count()).thenReturn(countRes);
        when(userRepository.count()).thenReturn(countRes);
        when(skillRepository.count()).thenReturn(countRes);

        CountDTO result = adminService.getAllCounts();

        verify(companyRepository).count();
        verify(callsignRepository).count();
        verify(platformRepository).count();
        verify(gameRepository).count();
        verify(heroRepository).count();
        verify(userRepository).count();
        verify(skillRepository).count();


        assertNotNull(result);
        assertEquals(countRes, result.getCompanies());
        assertEquals(countRes, result.getCallsigns());
        assertEquals(countRes, result.getPlatforms());
        assertEquals(countRes, result.getGames());
        assertEquals(countRes, result.getHeroes());
        assertEquals(countRes, result.getUsers());
        assertEquals(countRes, result.getSkills());

    }

}
