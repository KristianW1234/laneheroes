package com.personal.laneheroes.specifications;

import com.personal.laneheroes.entities.Hero;
import com.personal.laneheroes.enums.Gender;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class HeroSpecification {

    public static Specification<Hero> withFilters(
            String heroName,
            String heroTitle,
            Gender heroGender,
            Long gameId,
            String alternateName
    ) {
        return (Root<Hero> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction(); // Start with WHERE 1=1

            if (heroName != null && !heroName.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("heroName")), "%" + heroName.toLowerCase() + "%"));
            }

            if (heroTitle != null && !heroTitle.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("heroTitle")), "%" + heroTitle.toLowerCase() + "%"));
            }

            if (heroGender != null) {
                p = cb.and(p, cb.equal(root.get("heroGender"), heroGender)); // now uses enum
            }

            if (alternateName != null && !alternateName.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("alternateName")), "%" + alternateName.toLowerCase() + "%"));
            }

            if (gameId != null) {
                p = cb.and(p, cb.equal(root.get("game").get("id"), gameId));
            }

            return p;
        };
    }
}
