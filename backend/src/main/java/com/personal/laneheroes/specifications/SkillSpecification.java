package com.personal.laneheroes.specifications;

import com.personal.laneheroes.entities.Skill;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SkillSpecification {

    public static Specification<Skill> withFilters(
            String skillName,
            Long heroId
    ) {
        return (Root<Skill> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction(); // Start with WHERE 1=1

            if (skillName != null && !skillName.isBlank()) {
                p = cb.and(p, cb.like(cb.lower(root.get("skillName")), "%" + skillName.toLowerCase() + "%"));
            }

            if (heroId != null) {
                p = cb.and(p, cb.equal(root.get("hero").get("id"), heroId));
            }
            return p;
        };
    }
}
