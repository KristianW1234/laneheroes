package com.personal.laneheroes.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.repositories.CompanyRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CompanyService;
import com.personal.laneheroes.specifications.CompanySpecification;
import com.personal.laneheroes.utilities.ResponseMessages;
import com.personal.laneheroes.utilities.Utility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    private final ObjectMapper objectMapper;

    @Value("${image-dir}")
    private String imageDir;

    @Override
    public ResponseWrapper<Company> addCompany(Company company, MultipartFile imgFile) {
        Company dbCom = new Company();
        try {
            if (company.getCompanyName() != null){
                dbCom.setCompanyName(company.getCompanyName());
            } else {
                return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                        + ResponseMessages.ADD_FAIL,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (imgFile != null && !imgFile.isEmpty()){
                ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "company");
                if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                    dbCom.setImgIcon(uploadResult.getData());
                }

            }

            companyRepository.save(dbCom);
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.ADD_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.ADD_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

    }

    @Override
    public ResponseWrapper<Company> updateCompany(Company company, MultipartFile imgFile) {
        Optional<Company> companyPresence = companyRepository.findById(company.getId());
        if (companyPresence.isEmpty()){
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                + ResponseMessages.UPDATE_FAIL,
                ResponseMessages.FAIL_STATUS, null);
        }
        Company dbCom = companyPresence.get();

        try {
            if (company.getCompanyName() != null){
                dbCom.setCompanyName(company.getCompanyName());
            } else {
                return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                        + ResponseMessages.UPDATE_FAIL,
                        ResponseMessages.FAIL_STATUS, null);
            }

            if (imgFile != null && !imgFile.isEmpty()){
                ResponseWrapper<String> uploadResult = Utility.uploadFile(imgFile, imageDir, "company");
                if (uploadResult.getStatus().equals(ResponseMessages.SUCCESS_STATUS)){
                    dbCom.setImgIcon(uploadResult.getData());
                }

            }

            companyRepository.save(dbCom);
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.UPDATE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.UPDATE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }

    }

    @Override
    public ResponseWrapper<Company> deleteCompany(Long id) {
        Optional<Company> companyPresence = companyRepository.findById(id);
        if (companyPresence.isPresent()){
            Company dbCom = companyPresence.get();
            companyRepository.delete(dbCom);
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.DELETE_SUCCESS,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } else {
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + ResponseMessages.DELETE_FAIL,
                    ResponseMessages.FAIL_STATUS, null);
        }
    }

    @Override
    public ResponseWrapper<List<Company>> getAllCompanies() {
        List<Company> list = companyRepository.findAll();
        if (!list.isEmpty()){
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + list.size() + " ";
            if (list.size() > 1){
                successMessage += ResponseMessages.COMPANY_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.COMPANY_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, list);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS,  ResponseMessages.SUCCESS_STATUS, list);
        }
    }

    @Override
    public ResponseWrapper<Company> getCompanyById(Long id) {
        Optional<Company> companyPresence = companyRepository.findById(id);
        return companyPresence.map(
                company -> new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE
                        + " " + ResponseMessages.FOUND,
                        ResponseMessages.SUCCESS_STATUS, company))
                .orElseGet(() -> new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE
                        + " " + ResponseMessages.NOT_FOUND,
                        ResponseMessages.FAIL_STATUS, null));
    }

    @Override
    public ResponseWrapper<PagedResponse<Company>> searchCompanies(String name, Pageable pageable) {
        Specification<Company> spec = CompanySpecification.withFilters(name);
        Page<Company> resultPage = companyRepository.findAll(spec, pageable);
        PagedResponse<Company> pagedResponse = new PagedResponse<>(resultPage);

        if (resultPage.hasContent()) {
            String successMessage = ResponseMessages.SEARCH_RESULTS + ": " + resultPage.getSize() + " ";
            if (resultPage.getSize() > 1) {
                successMessage += ResponseMessages.COMPANY_PLURAL.toLowerCase();
            } else {
                successMessage += ResponseMessages.COMPANY_SINGLE.toLowerCase();
            }
            return new ResponseWrapper<>(successMessage, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        } else {
            return new ResponseWrapper<>(ResponseMessages.NO_RESULTS, ResponseMessages.SUCCESS_STATUS, pagedResponse);
        }

    }

    @Override
    public ResponseWrapper<UploadResult> uploadCompaniesFromExcel(String excelFile) {
        int totalAdded = 0;
        try (
                FileInputStream inputStream = new FileInputStream(excelFile);
                Workbook workbook = new XSSFWorkbook(inputStream);
        ) {
            List<Company> comps = companyRepository.findAll();
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next(); // skip the header row
            Company comp;

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                comp = new Company();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    if (columnIndex == 0){
                        comp.setCompanyName(nextCell.getStringCellValue());
                    } else if (columnIndex == 1){
                        comp.setImgIcon(nextCell.getStringCellValue());
                    }
                }

                if (!companyCopyCheck(comps, comp.getCompanyName())) {
                    totalAdded++;
                    companyRepository.save(comp);
                }
            }
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.BATCH_FAIL , ResponseMessages.FAIL_STATUS, UploadResult.error(ex.getMessage()));
        }
        return new ResponseWrapper<>(ResponseMessages.BATCH_SUCCESS , ResponseMessages.SUCCESS_STATUS, UploadResult.success(totalAdded));
    }

    @Override
    public void uploadInitCompaniesFromJSON(String path) throws IOException {
        if (companyRepository.count() > 0) return;

        InputStream input = new FileInputStream(path);
        List<Company> companies = objectMapper.readValue(input, new TypeReference<>() {});
        companyRepository.saveAll(companies);

    }

    private boolean companyCopyCheck(List<Company> comps, String name) {
        for (Company comp : comps) {
            if (comp.getCompanyName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}


