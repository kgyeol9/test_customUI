package com.example.airwx.repo;

import com.example.airwx.domain.AqStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AqStationRepository extends JpaRepository<AqStation, Long> {
    Optional<AqStation> findByStationNameAndSido(String stationName, String sido);
}
