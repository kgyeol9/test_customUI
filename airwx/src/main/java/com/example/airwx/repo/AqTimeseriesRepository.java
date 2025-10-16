package com.example.airwx.repo;

import com.example.airwx.domain.AqStation;
import com.example.airwx.domain.AqTimeseries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AqTimeseriesRepository extends JpaRepository<AqTimeseries, Long> {
    boolean existsByStationAndObservedAt(AqStation station, LocalDateTime observedAt);
}
