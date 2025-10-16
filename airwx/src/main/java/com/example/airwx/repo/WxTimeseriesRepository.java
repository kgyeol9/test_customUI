package com.example.airwx.repo;

import com.example.airwx.domain.Region;
import com.example.airwx.domain.WxTimeseries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface WxTimeseriesRepository extends JpaRepository<WxTimeseries, Long> {
    boolean existsByRegionAndForecastTime(Region region, LocalDateTime forecastTime);
}
