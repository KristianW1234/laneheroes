package com.personal.laneheroes.specifications;

import com.personal.laneheroes.entities.Company;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class CompanySpecification {

    public static Specification<Company> withFilters(
            String companyName
    ) {
        return (Root<Company> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction(); // Start with WHERE 1=1

            if (companyName != null && !companyName.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("companyName")), "%" + companyName.toLowerCase() + "%"));
            }

            return p;
        };
    }
}
