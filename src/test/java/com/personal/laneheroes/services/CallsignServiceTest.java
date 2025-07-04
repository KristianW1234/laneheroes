package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.repositories.CallsignRepository;
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
public class CallsignServiceTest {

    @Autowired
    private CallsignService callsignService;

    @Autowired
    private CallsignRepository callsignRepository;

    @BeforeEach
    void resetMocks() {
        reset(callsignRepository);
    }

    Callsign setupCallsign(){
        Callsign callsign = new Callsign();
        callsign.setCallsign("Hero");
        callsign.setCallsignPlural("Heroes");
        return callsign;
    }

    /*
     *
     * ADD CALLSIGN TESTS
     *
     * */

    @Test
    void addCallsign_test_1(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<Callsign> trial = callsignService.addCallsign(callsign);
        verify(callsignRepository).save(any(Callsign.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addCallsign_test_2(){
        Callsign callsign = setupCallsign();
        callsign.setCallsign(null);
        ResponseWrapper<Callsign> trial = callsignService.addCallsign(callsign);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addCallsign_test_3(){
        Callsign callsign = setupCallsign();
        callsign.setCallsign("");
        ResponseWrapper<Callsign> trial = callsignService.addCallsign(callsign);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addCallsign_test_4(){
        Callsign callsign = setupCallsign();
        callsign.setCallsignPlural(null);
        ResponseWrapper<Callsign> trial = callsignService.addCallsign(callsign);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addCallsign_test_5(){
        Callsign callsign = setupCallsign();
        callsign.setCallsignPlural("");
        ResponseWrapper<Callsign> trial = callsignService.addCallsign(callsign);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE CALLSIGN TESTS
     *
     * */

    @Test
    void updateCallsign_test_1(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<Callsign> trial = callsignService.updateCallsign(callsign);
        verify(callsignRepository).save(any(Callsign.class));
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateCallsign_test_2(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Callsign> trial = callsignService.updateCallsign(callsign);
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateCallsign_test_3(){
        Callsign callsign = setupCallsign();
        callsign.setCallsign(null);
        callsign.setCallsignPlural(null);
        ResponseWrapper<Callsign> trial = callsignService.updateCallsign(callsign);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateCallsign_test_4(){
        Callsign callsign = setupCallsign();
        callsign.setCallsignPlural(null);
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<Callsign> trial = callsignService.updateCallsign(callsign);
        verify(callsignRepository).save(any(Callsign.class));
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateCallsign_test_5(){
        Callsign callsign = setupCallsign();
        callsign.setCallsign(null);
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<Callsign> trial = callsignService.updateCallsign(callsign);
        verify(callsignRepository).save(any(Callsign.class));
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE CALLSIGN TESTS
     *
     * */

    @Test
    void deleteCallsign_test_1(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        ResponseWrapper<Callsign> trial = callsignService.deleteCallsign(any());
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void deleteCallsign_test_2(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Callsign> trial = callsignService.deleteCallsign(any());
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * GET ALL CALLSIGNS TESTS
     *
     * */

    @Test
    void getAllCallsigns_test_1(){
        Callsign callsign = setupCallsign();
        Callsign callsign2 = setupCallsign();

        List<Callsign> callsigns = new ArrayList<>();

        callsigns.add(callsign);
        callsigns.add(callsign2);

        when(callsignRepository.findAll()).thenReturn(callsigns);

        ResponseWrapper<List<Callsign>> trial = callsignService.getAllCallsigns();
        verify(callsignRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllCallsigns_test_2(){
        Callsign callsign = setupCallsign();

        List<Callsign> callsigns = new ArrayList<>();

        callsigns.add(callsign);

        when(callsignRepository.findAll()).thenReturn(callsigns);

        ResponseWrapper<List<Callsign>> trial = callsignService.getAllCallsigns();
        verify(callsignRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllCallsigns_test_3(){

        List<Callsign> callsigns = new ArrayList<>();


        when(callsignRepository.findAll()).thenReturn(callsigns);

        ResponseWrapper<List<Callsign>> trial = callsignService.getAllCallsigns();
        verify(callsignRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * GET CALLSIGN BY ID TESTS
     *
     * */

    @Test
    void getCallsignById_test_1(){
        Callsign callsign = setupCallsign();
        when(callsignRepository.findById(any())).thenReturn(Optional.of(callsign));
        ResponseWrapper<Callsign> trial = callsignService.getCallsignById(any());
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void getCallsignById_test_2(){
        when(callsignRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Callsign> trial = callsignService.getCallsignById(any());
        verify(callsignRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD CALLSIGNS FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadCallsignsFromExcel_test_1() throws URISyntaxException {
        Callsign callsign = setupCallsign();
        List<Callsign> callsigns = new ArrayList<>();
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-callsigns.xlsx").toURI());

        when(callsignRepository.findAll()).thenReturn(callsigns);
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<UploadResult> response = callsignService.uploadCallsignsFromExcel(path.toString());

        verify(callsignRepository).findAll();
        verify(callsignRepository, times(2)).save(any(Callsign.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadCallsignsFromExcel_test_2() throws URISyntaxException {
        Callsign callsign = setupCallsign();
        List<Callsign> callsigns = new ArrayList<>();

        callsigns.add(callsign);
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-callsigns.xlsx").toURI());

        when(callsignRepository.findAll()).thenReturn(callsigns);
        when(callsignRepository.save(any(Callsign.class))).thenReturn(callsign);
        ResponseWrapper<UploadResult> response = callsignService.uploadCallsignsFromExcel(path.toString());

        verify(callsignRepository).findAll();
        verify(callsignRepository).save(any(Callsign.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadCallsignsFromExcel_test_3() throws URISyntaxException {
        Callsign callsign = setupCallsign();
        List<Callsign> callsigns = new ArrayList<>();

        callsigns.add(callsign);
        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-callsigns.xlsx").toURI());

        when(callsignRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = callsignService.uploadCallsignsFromExcel(path.toString());

        verify(callsignRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT CALLSIGNS FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitCallsignsFromJSON_test_1() throws URISyntaxException, IOException {
        List<Callsign> callsigns = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-InitCallsigns.json").toURI());

        when(callsignRepository.count()).thenReturn(0L);
        when(callsignRepository.saveAll(any())).thenReturn(callsigns);

        callsignService.uploadInitCallsignsFromJSON(path.toString());

        verify(callsignRepository).count();
        verify(callsignRepository).saveAll(any());
    }

    @Test
    void uploadInitCallsignsFromJSON_test_2() throws URISyntaxException, IOException {

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-InitCallsigns.json").toURI());

        when(callsignRepository.count()).thenReturn(1L);

        callsignService.uploadInitCallsignsFromJSON(path.toString());

        verify(callsignRepository).count();
    }
}
