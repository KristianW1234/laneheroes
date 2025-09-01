package com.personal.laneheroes.repositories;

import com.personal.laneheroes.entities.Company;
import com.personal.laneheroes.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    Optional<Game> findByGameNameIgnoreCase(String name);
}
