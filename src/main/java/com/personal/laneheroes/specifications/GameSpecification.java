package com.personal.laneheroes.specifications;

import com.personal.laneheroes.entities.Game;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class GameSpecification {

    public static Specification<Game> withFilters(
            String gameName,
            String gameCode,
            Long companyId,
            Long platformId,
            Long callsignId
    ) {
        return (Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            Predicate p = cb.conjunction(); // Start with WHERE 1=1

            if (gameName != null && !gameName.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("gameName")), "%" + gameName.toLowerCase() + "%"));
            }

            if (gameCode != null && !gameCode.isEmpty()) {
                p = cb.and(p, cb.like(cb.lower(root.get("gameCode")), "%" + gameCode.toLowerCase() + "%"));
            }

            if (companyId != null) {
                p = cb.and(p, cb.equal(root.get("company").get("id"), companyId));
            }

            if (platformId != null) {
                p = cb.and(p, cb.equal(root.get("platform").get("id"), platformId));
            }

            if (callsignId != null) {
                p = cb.and(p, cb.equal(root.get("callsign").get("id"), callsignId));
            }

            return p;
        };
    }
}
