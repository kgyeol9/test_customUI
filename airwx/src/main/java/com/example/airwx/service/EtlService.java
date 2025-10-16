package com.example.airwx.service;

import com.example.airwx.domain.*;
import com.example.airwx.provider.AirProvider;
import com.example.airwx.provider.WeatherProvider;
import com.example.airwx.repo.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EtlService {

    private final WeatherProvider weatherProvider;
    private final AirProvider airProvider;

    private final RegionRepository regionRepository;
    private final WxTimeseriesRepository wxRepository;

    private final AqStationRepository stationRepository;
    private final AqTimeseriesRepository aqRepository;

    public EtlService(WeatherProvider weatherProvider,
                      AirProvider airProvider,
                      RegionRepository regionRepository,
                      WxTimeseriesRepository wxRepository,
                      AqStationRepository stationRepository,
                      AqTimeseriesRepository aqRepository) {
        this.weatherProvider = weatherProvider;
        this.airProvider = airProvider;
        this.regionRepository = regionRepository;
        this.wxRepository = wxRepository;
        this.stationRepository = stationRepository;
        this.aqRepository = aqRepository;
    }

    // ===== 날씨 ETL =====
    @Transactional
    public int ingestWeatherWithResult(double lat, double lon, String sido, String sigungu, boolean useMock) {
        Region region = regionRepository
                .findBySidoAndSigunguAndDong(sido, sigungu, null)
                .orElseGet(() -> regionRepository.save(new Region(sido, sigungu, null, lat, lon)));

        List<WeatherProvider.WxHourly> items =
                useMock ? buildWxMock() : weatherProvider.fetchHourly(lat, lon);

        int inserted = 0;
        for (WeatherProvider.WxHourly h : items) {
            if (!wxRepository.existsByRegionAndForecastTime(region, h.getForecastTime())) {
                WxTimeseries row = new WxTimeseries(
                        region, h.getForecastTime(),
                        h.getTemp(), h.getHumidity(), h.getPop(), h.getWindSpeed()
                );
                wxRepository.save(row);
                inserted++;
            }
        }
        return inserted;
    }

    private static List<WeatherProvider.WxHourly> buildWxMock() {
        List<WeatherProvider.WxHourly> list = new ArrayList<>();
        LocalDateTime base = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        for (int i=0;i<6;i++) {
            LocalDateTime t = base.plusHours(i);
            list.add(new WeatherProvider.WxHourly(t, 20.0 + i, 60, 10, 2.5));
        }
        return list;
    }

    // ===== 대기질 ETL =====
    @Transactional
    public int ingestAirWithResult(double lat, double lon, String sido, boolean useMock) {
        List<AirProvider.StationWithSamples> items =
                useMock ? buildAirMock() : airProvider.fetchLatestNear(lat, lon, 10000); // 10km

        int inserted = 0;
        for (AirProvider.StationWithSamples s : items) {
            AirProvider.AirPoint p = s.getStation();

            AqStation st = stationRepository
                    .findByStationNameAndSido(p.getStationName(), sido)
                    .orElseGet(() -> stationRepository.save(new AqStation(
                            p.getStationName(), sido, p.getLat(), p.getLon()
                    )));

            for (AirProvider.AirSample sm : s.getSamples()) {
                if (!aqRepository.existsByStationAndObservedAt(st, sm.getObservedAt())) {
                    aqRepository.save(new AqTimeseries(
                            st, sm.getObservedAt(),
                            sm.getPm10(), sm.getPm25(), sm.getO3(), sm.getNo2(), sm.getSo2(), sm.getCo()
                    ));
                    inserted++;
                }
            }
        }
        return inserted;
    }

    private static List<AirProvider.StationWithSamples> buildAirMock() {
        List<AirProvider.StationWithSamples> list = new ArrayList<>();
        LocalDateTime base = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        List<AirProvider.AirSample> samples = new ArrayList<>();
        for (int i=0;i<3;i++){
            samples.add(new AirProvider.AirSample(base.plusHours(i), 20+i, 10+i, 0.03, 0.02, 0.004, 0.5));
        }
        list.add(new AirProvider.StationWithSamples(
                new AirProvider.AirPoint("MockStation-1","",36.33,127.43), samples));
        return list;
    }
}
