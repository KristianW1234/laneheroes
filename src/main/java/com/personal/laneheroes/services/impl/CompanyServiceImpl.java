package com.personal.laneheroes.services.impl;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.dto.UploadResult;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.repositories.CompanyRepository;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CompanyService;
import com.personal.laneheroes.specifications.CompanySpecification;
import com.personal.laneheroes.utilities.ResponseMessages;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service("CompanyServiceImpl")
@Transactional
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public ResponseWrapper<Company> addOrUpdateCompany(Company company, boolean isUpdate) {
        Company dbCom = new Company();
        String successMsg = ResponseMessages.ADD_SUCCESS;
        String failMsg = ResponseMessages.ADD_FAIL;
        if (isUpdate){
            successMsg = ResponseMessages.UPDATE_SUCCESS;
            failMsg = ResponseMessages.UPDATE_FAIL;
            Optional<Company> companyPresence = companyRepository.findById(company.getId());
            if (companyPresence.isEmpty()){
                return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }
            dbCom = companyPresence.get();
        }

        try {
            if (company.getCompanyName() != null){
                dbCom.setCompanyName(company.getCompanyName());
            } else {
                return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                        + failMsg,
                        ResponseMessages.FAIL_STATUS, null);
            }

            companyRepository.save(dbCom);
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + successMsg,
                    ResponseMessages.SUCCESS_STATUS, dbCom);
        } catch (Exception ex){
            return new ResponseWrapper<>(ResponseMessages.COMPANY_SINGLE + " "
                    + failMsg,
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
        try {
            List<Company> comps = companyRepository.findAll();
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
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
                    switch (columnIndex) {
                        case 0:
                            comp.setCompanyName(nextCell.getStringCellValue());
                            break;
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

    private boolean companyCopyCheck(List<Company> comps, String name) {
        for (Company comp : comps) {
            if (comp.getCompanyName().equals(name)) {
                return true;
            }
        }
        return false;
    }


}


