package com.personal.laneheroes.repositories;

import com.personal.laneheroes.entities.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long>, JpaSpecificationExecutor<Hero> {
    Optional<Hero> findByHeroNameIgnoreCase(String name);
    Optional<Hero> findByHeroCodeIgnoreCase(String code);
}
