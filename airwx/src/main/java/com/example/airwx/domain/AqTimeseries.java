package com.example.airwx.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "aq_timeseries",
        uniqueConstraints = @UniqueConstraint(name = "uk_aq", columnNames = {"station_id", "observed_at"})
)
public class AqTimeseries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "station_id")
    private AqStation station;

    @Column(name = "observed_at", nullable = false)
    private LocalDateTime observedAt;

    private Integer pm10;
    private Integer pm25;
    private Double o3;
    private Double no2;
    private Double so2;
    private Double co;

    public AqTimeseries() {}

    public AqTimeseries(AqStation station, LocalDateTime observedAt,
                        Integer pm10, Integer pm25, Double o3, Double no2, Double so2, Double co) {
        this.station = station;
        this.observedAt = observedAt;
        this.pm10 = pm10;
        this.pm25 = pm25;
        this.o3 = o3;
        this.no2 = no2;
        this.so2 = so2;
        this.co = co;
    }

    public Long getTsId() { return tsId; }
    public void setTsId(Long tsId) { this.tsId = tsId; }

    public AqStation getStation() { return station; }
    public void setStation(AqStation station) { this.station = station; }

    public LocalDateTime getObservedAt() { return observedAt; }
    public void setObservedAt(LocalDateTime observedAt) { this.observedAt = observedAt; }

    public Integer getPm10() { return pm10; }
    public void setPm10(Integer pm10) { this.pm10 = pm10; }

    public Integer getPm25() { return pm25; }
    public void setPm25(Integer pm25) { this.pm25 = pm25; }

    public Double getO3() { return o3; }
    public void setO3(Double o3) { this.o3 = o3; }

    public Double getNo2() { return no2; }
    public void setNo2(Double no2) { this.no2 = no2; }

    public Double getSo2() { return so2; }
    public void setSo2(Double so2) { this.so2 = so2; }

    public Double getCo() { return co; }
    public void setCo(Double co) { this.co = co; }
}
