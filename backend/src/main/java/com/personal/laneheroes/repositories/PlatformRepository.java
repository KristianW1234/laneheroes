package com.personal.laneheroes.repositories;

import com.personal.laneheroes.entities.Callsign;
import com.personal.laneheroes.entities.Platform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    List<Platform> findByPlatformNameContainingIgnoreCase(String name);
    Page<Platform> findByPlatformNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Platform> findByPlatformNameIgnoreCase(String name);
}
