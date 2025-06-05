package com.personal.laneheroes.controllers;

import com.personal.laneheroes.dto.PagedResponse;
import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.response.ResponseWrapper;
import com.personal.laneheroes.services.CompanyService;
import com.personal.laneheroes.utilities.Utility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laneHeroes/company")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

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

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper<Company>> addCompany(@Valid @RequestBody Company company) {
        ResponseWrapper<Company> response = companyService.addCompany(company);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update")
    public ResponseEntity<ResponseWrapper<Company>> updateCompany(@Valid @RequestBody Company company) {
        ResponseWrapper<Company> response = companyService.updateCompany(company);
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
