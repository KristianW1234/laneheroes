package com.personal.laneheroes.services;

import com.personal.laneheroes.config.TestMockConfig;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.repositories.CompanyRepository;
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
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void resetMocks() {
        reset(companyRepository);
    }

    Company setupCompany(){
        Company company = new Company();
        company.setId(1L);
        company.setCompanyName("This Company");
        return company;
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
     * ADD COMPANY TESTS
     *
     * */

    @Test
    void addCompany_test_1(){
        MockMultipartFile mockFile = setupFile("company-icon.png", "fake image content".getBytes());
        Company company = setupCompany();
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.addCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addCompany_test_2(){
        Company company = setupCompany();
        company.setCompanyName(null);
        ResponseWrapper<Company> trial = companyService.addCompany(company, null);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addCompany_test_3(){
        Company company = setupCompany();
        company.setCompanyName("");
        ResponseWrapper<Company> trial = companyService.addCompany(company, null);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void addCompany_test_4(){

        Company company = setupCompany();
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.addCompany(company, null);
        verify(companyRepository).save(any(Company.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addCompany_test_5(){
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Company company = setupCompany();
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.addCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void addCompany_test_6(){
        MockMultipartFile mockFile = setupFile("company-icon.doc", "fake image content".getBytes());
        Company company = setupCompany();
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.addCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPDATE COMPANY TESTS
     *
     * */

    @Test
    void updateCompany_test_1(){
        MockMultipartFile mockFile = setupFile("company-icon.png", "fake image content".getBytes());
        Company company = setupCompany();
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.updateCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateCompany_test_2(){
        Company company = setupCompany();
        when(companyRepository.findById(any())).thenReturn(Optional.empty());
        ResponseWrapper<Company> trial = companyService.updateCompany(company, null);
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateCompany_test_3(){
        Company company = setupCompany();
        company.setCompanyName(null);
        ResponseWrapper<Company> trial = companyService.updateCompany(company, null);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    @Test
    void updateCompany_test_4(){
        MockMultipartFile mockFile = setupFile("", new byte[0]);
        Company company = setupCompany();
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.updateCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateCompany_test_5(){
        MockMultipartFile mockFile = setupFile("company-icon.doc", "fake image content".getBytes());
        Company company = setupCompany();
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.updateCompany(company, mockFile);
        verify(companyRepository).save(any(Company.class));
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void updateCompany_test_6(){
        Company company = setupCompany();
        when(companyRepository.findById(any())).thenReturn(Optional.of(company));
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<Company> trial = companyService.updateCompany(company, null);
        verify(companyRepository).save(any(Company.class));
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }



    @Test
    void updateCompany_test_7(){
        Company company = setupCompany();
        company.setCompanyName("");
        ResponseWrapper<Company> trial = companyService.updateCompany(company, null);
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());
    }

    /*
     *
     * DELETE COMPANY TESTS
     *
     * */

    @Test
    void deleteCompany_test_1(){
        Company company = setupCompany();

        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        ResponseWrapper<Company> trial = companyService.deleteCompany(any());

        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void deleteCompany_test_2(){

        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Company> trial = companyService.deleteCompany(any());
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    /*
     *
     * GET ALL COMPANIES TESTS
     *
     * */

    @Test
    void getAllCompanies_test_1(){
        Company company = setupCompany();
        Company company2 = setupCompany();

        List<Company> companies = new ArrayList<>();

        companies.add(company);
        companies.add(company2);

        when(companyRepository.findAll()).thenReturn(companies);

        ResponseWrapper<List<Company>> trial = companyService.getAllCompanies();
        verify(companyRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllCompanies_test_2(){
        Company company = setupCompany();

        List<Company> companies = new ArrayList<>();

        companies.add(company);

        when(companyRepository.findAll()).thenReturn(companies);

        ResponseWrapper<List<Company>> trial = companyService.getAllCompanies();
        verify(companyRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getAllCompanies_test_3(){

        List<Company> companies = new ArrayList<>();


        when(companyRepository.findAll()).thenReturn(companies);

        ResponseWrapper<List<Company>> trial = companyService.getAllCompanies();
        verify(companyRepository).findAll();
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    /*
     *
     * GET COMPANY BY ID TESTS
     *
     * */

    @Test
    void getCompanyById_test_1(){
        Company company = setupCompany();

        when(companyRepository.findById(any())).thenReturn(Optional.of(company));

        ResponseWrapper<Company> trial = companyService.getCompanyById(company.getId());

        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());

    }

    @Test
    void getCompanyById_test_2(){

        when(companyRepository.findById(any())).thenReturn(Optional.empty());

        ResponseWrapper<Company> trial = companyService.getCompanyById(any());
        verify(companyRepository).findById(any());
        assertNotNull(trial);
        assertEquals(ResponseMessages.FAIL_STATUS, trial.getStatus());

    }

    /*
     *
     * SEARCH COMPANIES TESTS
     *
     * */

    @Test
    void searchCompanies_test_1(){
        Company company = setupCompany();
        Company company2 = setupCompany();

        List<Company> companies = new ArrayList<>();

        companies.add(company);
        companies.add(company2);

        Page<Company> companyPage = new PageImpl<>(companies, PageRequest.of(0,10), 1);

        when(companyRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(companyPage);

        ResponseWrapper<PagedResponse<Company>> trial = companyService.searchCompanies(
                "company-name",  PageRequest.of(0, 10));

        verify(companyRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchCompanies_test_2(){
        Company company = setupCompany();

        List<Company> companies = new ArrayList<>();

        companies.add(company);

        Page<Company> companyPage = new PageImpl<>(companies, PageRequest.of(0,10), 1);

        when(companyRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(companyPage);

        ResponseWrapper<PagedResponse<Company>> trial = companyService.searchCompanies(
                "company-name",  PageRequest.of(0, 10));

        verify(companyRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    @Test
    void searchCompanies_test_3(){

        List<Company> companies = new ArrayList<>();


        Page<Company> companyPage = new PageImpl<>(companies, PageRequest.of(0,10), 0);

        when(companyRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(companyPage);

        ResponseWrapper<PagedResponse<Company>> trial = companyService.searchCompanies(
                "company-name",  PageRequest.of(0, 10));

        verify(companyRepository).findAll(any(Specification.class), any(Pageable.class));
        assertNotNull(trial);
        assertEquals(ResponseMessages.SUCCESS_STATUS, trial.getStatus());
    }

    /*
     *
     * UPLOAD COMPANIES FROM EXCEL TESTS
     *
     * */

    @Test
    void uploadCompaniesFromExcel_test_1() throws URISyntaxException {
        Company company = setupCompany();

        List<Company> companies = new ArrayList<>();

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-companies.xlsx").toURI());

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<UploadResult> response = companyService.uploadCompaniesFromExcel(path.toString());

        verify(companyRepository).findAll();
        verify(companyRepository, times(2)).save(any(Company.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadCompaniesFromExcel_test_2() throws URISyntaxException {
        Company company = setupCompany();
        List<Company> companies = new ArrayList<>();

        companies.add(company);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-companies.xlsx").toURI());

        when(companyRepository.findAll()).thenReturn(companies);
        when(companyRepository.save(any(Company.class))).thenReturn(company);
        ResponseWrapper<UploadResult> response = companyService.uploadCompaniesFromExcel(path.toString());

        verify(companyRepository).findAll();
        verify(companyRepository).save(any(Company.class));
        assertNotNull(response);
        assertEquals(ResponseMessages.SUCCESS_STATUS, response.getStatus());
    }

    @Test
    void uploadCompaniesFromExcel_test_3() throws URISyntaxException {
        Company company = setupCompany();
        List<Company> companies = new ArrayList<>();

        companies.add(company);

        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-companies.xlsx").toURI());

        when(companyRepository.findAll()).thenThrow(new QueryTimeoutException("DB timed out"));
        ResponseWrapper<UploadResult> response = companyService.uploadCompaniesFromExcel(path.toString());

        verify(companyRepository).findAll();
        assertNotNull(response);
        assertEquals(ResponseMessages.FAIL_STATUS, response.getStatus());
    }

    /*
     *
     * UPLOAD INIT COMPANIES FROM JSON TESTS
     *
     * */

    @Test
    void uploadInitCompaniesFromJSON_test_1() throws URISyntaxException, IOException {

        List<Company> companies = new ArrayList<>();


        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-initCompanies.json").toURI());

        when(companyRepository.count()).thenReturn(0L);
        when(companyRepository.saveAll(any())).thenReturn(companies);

        companyService.uploadInitCompaniesFromJSON(path.toString());

        verify(companyRepository).count();
        verify(companyRepository).saveAll(any());

    }

    @Test
    void uploadInitCompaniesFromJSON_test_2() throws URISyntaxException, IOException {


        Path path = Paths.get(getClass().getClassLoader().getResource("data/test-initCompanies.json").toURI());

        when(companyRepository.count()).thenReturn(1L);

        companyService.uploadInitCompaniesFromJSON(path.toString());

        verify(companyRepository).count();

    }
}
