package com.personal.laneheroes.specifications;

import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.entities.User;
import com.personal.laneheroes.enums.Role;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> withFilters(
            String userName,
            Role userRole
    ) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction(); // Start with WHERE 1=1

            if (userName != null && !userName.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("userName")), "%" + userName.toLowerCase() + "%"));
            }

            if (userRole != null) {
                p = cb.and(p, cb.equal(root.get("userRole"), userRole)); // now uses enum
            }

            return p;
        };
    }
}
