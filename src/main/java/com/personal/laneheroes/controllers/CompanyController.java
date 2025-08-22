package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CompanyService;
import com.personal.laneheroes.utilities.Utility;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/company")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseWrapper<List<Company>>> getAllCompanies() {
        ResponseWrapper<List<Company>> response = companyService.getAllCompanies();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<Company>> getOneCompany(@PathVariable Long id) {
        ResponseWrapper<Company> response = companyService.getCompanyById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Company>> addCompany(
            @RequestPart("company") Company company,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {
        ResponseWrapper<Company> response = companyService.addCompany(company, imgFile);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseWrapper<Company>> updateCompany(
            @RequestPart("company") Company company,
            @RequestPart(value="imgFile", required = false) MultipartFile imgFile) {
        ResponseWrapper<Company> response = companyService.updateCompany(company,imgFile);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<Company>> deleteCompany(@PathVariable Long id) {
        ResponseWrapper<Company> response = companyService.deleteCompany(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseWrapper<PagedResponse<Company>>> searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        ResponseWrapper<PagedResponse<Company>> response = companyService.searchCompanies(name, Utility.setupPageable(page, size, sortBy, sortOrder));
        return ResponseEntity.ok(response);
    }
}
