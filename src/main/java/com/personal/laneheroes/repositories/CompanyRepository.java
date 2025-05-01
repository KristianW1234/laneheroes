package com.personal.laneheroes.repositories;

import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    List<Company> findByCompanyNameContainingIgnoreCase(String name);
    Page<Company> findByCompanyNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Company> findByCompanyNameIgnoreCase(String name);
}
