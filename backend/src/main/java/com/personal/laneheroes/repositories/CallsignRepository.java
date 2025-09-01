package com.personal.laneheroes.repositories;

import com.personal.laneheroes.entities.Callsign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CallsignRepository extends JpaRepository<Callsign, Long> {
    List<Callsign> findByCallsignContainingIgnoreCase(String callsign);
    Page<Callsign> findByCallsignContainingIgnoreCase(String callsign, Pageable pageable);
    Optional<Callsign> findByCallsignIgnoreCase(String callsign);
}
