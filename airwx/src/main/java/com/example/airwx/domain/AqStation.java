package com.example.airwx.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "aq_station",
        uniqueConstraints = @UniqueConstraint(name = "uk_station", columnNames = {"stationName", "sido"})
)
public class AqStation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stationId;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String sido;

    private Double lat;
    private Double lon;

    public AqStation() {}

    public AqStation(String stationName, String sido, Double lat, Double lon) {
        this.stationName = stationName;
        this.sido = sido;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }

    public String getStationName() { return stationName; }
    public void setStationName(String stationName) { this.stationName = stationName; }

    public String getSido() { return sido; }
    public void setSido(String sido) { this.sido = sido; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
}
