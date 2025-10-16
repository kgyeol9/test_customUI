package com.example.airwx.provider;

import java.time.LocalDateTime;
import java.util.List;

public interface AirProvider {

    // 관측소(측정소) 메타
    final class AirPoint {
        private final String stationName;
        private final String sido;
        private final Double lat;
        private final Double lon;
        public AirPoint(String stationName, String sido, Double lat, Double lon) {
            this.stationName = stationName; this.sido = sido; this.lat = lat; this.lon = lon;
        }
        public String getStationName() { return stationName; }
        public String getSido() { return sido; }
        public Double getLat() { return lat; }
        public Double getLon() { return lon; }
    }

    // 시계열 1건
    final class AirSample {
        private final LocalDateTime observedAt;
        private final Integer pm10, pm25;
        private final Double o3, no2, so2, co;
        public AirSample(LocalDateTime observedAt, Integer pm10, Integer pm25,
                         Double o3, Double no2, Double so2, Double co) {
            this.observedAt = observedAt; this.pm10 = pm10; this.pm25 = pm25;
            this.o3=o3; this.no2=no2; this.so2=so2; this.co=co;
        }
        public LocalDateTime getObservedAt() { return observedAt; }
        public Integer getPm10() { return pm10; }
        public Integer getPm25() { return pm25; }
        public Double getO3() { return o3; }
        public Double getNo2() { return no2; }
        public Double getSo2() { return so2; }
        public Double getCo() { return co; }
    }

    // 관측소 + 해당 시계열 묶음
    final class StationWithSamples {
        private final AirPoint station; private final List<AirSample> samples;
        public StationWithSamples(AirPoint station, List<AirSample> samples) {
            this.station = station; this.samples = samples;
        }
        public AirPoint getStation() { return station; }
        public List<AirSample> getSamples() { return samples; }
    }

    /** 중심좌표 주변 반경(m) 내 최신 관측값 조회 */
    List<StationWithSamples> fetchLatestNear(double lat, double lon, int radiusMeters);
}
