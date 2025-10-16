package com.example.airwx.domain;

import jakarta.persistence.*;

@Entity
@Table(
        name = "region",
        uniqueConstraints = @UniqueConstraint(name = "uk_region", columnNames = {"sido", "sigungu", "dong"})
)
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;

    @Column(nullable = false)
    private String sido;

    @Column(nullable = false)
    private String sigungu;

    private String dong;

    private Double lat;
    private Double lon;

    // ---- 기본 생성자 (JPA 필수) ----
    public Region() {}

    // ---- 전체 필드 생성자(선택) ----
    public Region(Long regionId, String sido, String sigungu, String dong, Double lat, Double lon) {
        this.regionId = regionId;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dong = dong;
        this.lat = lat;
        this.lon = lon;
    }

    // ---- 편의 생성자(필수값만) ----
    public Region(String sido, String sigungu, String dong, Double lat, Double lon) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.dong = dong;
        this.lat = lat;
        this.lon = lon;
    }

    // ---- Getter/Setter ----
    public Long getRegionId() { return regionId; }
    public void setRegionId(Long regionId) { this.regionId = regionId; }

    public String getSido() { return sido; }
    public void setSido(String sido) { this.sido = sido; }

    public String getSigungu() { return sigungu; }
    public void setSigungu(String sigungu) { this.sigungu = sigungu; }

    public String getDong() { return dong; }
    public void setDong(String dong) { this.dong = dong; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return lon; }
    public void setLon(Double lon) { this.lon = lon; }
}
