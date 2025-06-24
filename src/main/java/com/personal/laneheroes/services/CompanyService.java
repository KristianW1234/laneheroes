package com.personal.laneheroes.services;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.response.ResponseWrapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyService {
    ResponseWrapper<Company> addCompany(Company company, MultipartFile imgFile);

    ResponseWrapper<Company> updateCompany(Company company, MultipartFile imgFile);

    ResponseWrapper<Company> deleteCompany(Long id);

    ResponseWrapper<List<Company>> getAllCompanies();

    ResponseWrapper<Company> getCompanyById(Long id);

    ResponseWrapper<PagedResponse<Company>> searchCompanies(String name, Pageable pageable);

    ResponseWrapper<UploadResult> uploadCompaniesFromExcel(String excelFile);

    void uploadInitCompaniesFromJSON () throws IOException;
}
