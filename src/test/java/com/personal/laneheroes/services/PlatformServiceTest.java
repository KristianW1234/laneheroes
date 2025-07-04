package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.entities.Platform;
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
public class PlatformServiceTest {

    @Autowired
    private PlatformService platformService;

    @Autowired
    private PlatformRepository platformRepository;

    @BeforeEach
    void resetMocks() {
        reset(platformRepository);
    }

    Platform setupPlatform(){
        Platform platform = new Platform();
        platform.setId(1L);
        platform.setPlatformName("PC");
        return platform;
    }

    /*
     *
     * ADD PLATFORM TESTS
     *
     * */

    @Test
    void addPlatform_test_1(){
        Platform platform = setupPlatform();
        when(platformRepository.save(any(Platform.class))).thenReturn(platform);
        ResponseWrapper<Platform> trial = platformService.addPlatform(platform);
        verify(platformRepository).save(any(Platform.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addPlatform_test_2(){
        Platform platform = setupPlatform();
        platform.setPlatformName(null);
        ResponseWrapper<Platform> trial = platformService.addPlatform(platform);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addPlatform_test_3(){
        Platform platform = setupPlatform();
        platform.setPlatformName("");
        ResponseWrapper<Platform> trial = platformService.addPlatform(platform);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE PLATFORM TESTS
     *
     * */

    @Test
    void updatePlatform_test_1(){
        Platform platform = setupPlatform();
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        when(platformRepository.save(any(Platform.class))).thenReturn(platform);
        ResponseWrapper<Platform> trial = platformService.updatePlatform(platform);
        verify(platformRepository).findById(any());
        verify(platformRepository).save(any(Platform.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updatePlatform_test_2(){
        Platform platform = setupPlatform();
        when(platformRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Platform> trial = platformService.updatePlatform(platform);
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updatePlatform_test_3(){
        Platform platform = setupPlatform();
        platform.setPlatformName(null);
        ResponseWrapper<Platform> trial = platformService.updatePlatform(platform);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updatePlatform_test_4(){
        Platform platform = setupPlatform();
        platform.setPlatformName("");
        ResponseWrapper<Platform> trial = platformService.updatePlatform(platform);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE PLATFORM TESTS
     *
     * */

    @Test
    void deletePlatform_test_1(){
        Platform platform = setupPlatform();
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Platform> trial = platformService.deletePlatform(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void deletePlatform_test_2(){
        Platform platform = setupPlatform();
        when(platformRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Platform> trial = platformService.deletePlatform(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * GET ALL PLATFORMS TESTS
     *
     * */

    @Test
    void getAllPlatforms_test_1(){
        Platform platform = setupPlatform();
        Platform platform2 = setupPlatform();

        List<Platform> platforms = new ArrayList<>();

        platforms.add(platform);
        platforms.add(platform2);

        when(platformRepository.findAll()).thenReturn(platforms);

        ResponseWrapper<List<Platform>> trial = platformService.getAllPlatforms();
        verify(platformRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllPlatforms_test_2(){
        Platform platform = setupPlatform();

        List<Platform> platforms = new ArrayList<>();

        platforms.add(platform);

        when(platformRepository.findAll()).thenReturn(platforms);

        ResponseWrapper<List<Platform>> trial = platformService.getAllPlatforms();
        verify(platformRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllPlatforms_test_3(){

        List<Platform> platforms = new ArrayList<>();


        when(platformRepository.findAll()).thenReturn(platforms);

        ResponseWrapper<List<Platform>> trial = platformService.getAllPlatforms();
        verify(platformRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * GET PLATFORM BY ID TESTS
     *
     * */

    @Test
    void getPlatformById_test_1(){
        Platform platform = setupPlatform();
        when(platformRepository.findById(any())).thenReturn(Optional.of(platform));
        ResponseWrapper<Platform> trial = platformService.getPlatformById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getPlatformById_test_2(){
        when(platformRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Platform> trial = platformService.getPlatformById(any());
        verify(platformRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD PLATFORMS FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadPlatformsFromExcel_test_1() throws URISyntaxException {
        Platform platform = setupPlatform();
        List<Platform> platforms = new ArrayList<>();
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-platforms.xlsx").toURI());

        when(platformRepository.findAll()).thenReturn(platforms);
        when(platformRepository.save(any(Platform.class))).thenReturn(platform);
        ResponseWrapper<UploadResult> response = platformService.uploadPlatformsFromExcel(path.toString());

        verify(platformRepository).findAll();
        verify(platformRepository, times(2)).save(any(Platform.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadPlatformsFromExcel_test_2() throws URISyntaxException {
        Platform platform = setupPlatform();
        List<Platform> platforms = new ArrayList<>();

        platforms.add(platform);
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-platforms.xlsx").toURI());

        when(platformRepository.findAll()).thenReturn(platforms);
        when(platformRepository.save(any(Platform.class))).thenReturn(platform);
        ResponseWrapper<UploadResult> response = platformService.uploadPlatformsFromExcel(path.toString());

        verify(platformRepository).findAll();
        verify(platformRepository).save(any(Platform.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadPlatformsFromExcel_test_3() throws URISyntaxException {
        Platform platform = setupPlatform();
        List<Platform> platforms = new ArrayList<>();

        platforms.add(platform);
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-platforms.xlsx").toURI());

        when(platformRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = platformService.uploadPlatformsFromExcel(path.toString());

        verify(platformRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT PLATFORMS FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitPlatformsFromJSON_test_1() throws URISyntaxException, IOException {
        List<Platform> platforms = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-InitPlatforms.json").toURI());

        when(platformRepository.count()).thenReturn(0L);
        when(platformRepository.saveAll(any())).thenReturn(platforms);

        platformService.uploadInitPlatformsFromJSON(path.toString());

        verify(platformRepository).count();
        verify(platformRepository).saveAll(any());
    }

    @Test
    void uploadInitPlatformsFromJSON_test_2() throws URISyntaxException, IOException {

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-InitPlatforms.json").toURI());

        when(platformRepository.count()).thenReturn(1L);

        platformService.uploadInitPlatformsFromJSON(path.toString());

        verify(platformRepository).count();
    }
}
