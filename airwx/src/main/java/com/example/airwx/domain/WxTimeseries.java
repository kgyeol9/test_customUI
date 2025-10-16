package com.example.airwx.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "wx_timeseries",
        uniqueConstraints = @UniqueConstraint(name = "uk_wx", columnNames = {"region_id", "forecast_time"})
)
public class WxTimeseries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tsId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "forecast_time", nullable = false)
    private LocalDateTime forecastTime;

    private Double temp;
    private Integer humidity;
    private Integer pop;        // 강수확률 %
    private Double windSpeed;

    public WxTimeseries() {}

    public WxTimeseries(Region region, LocalDateTime forecastTime,
                        Double temp, Integer humidity, Integer pop, Double windSpeed) {
        this.region = region;
        this.forecastTime = forecastTime;
        this.temp = temp;
        this.humidity = humidity;
        this.pop = pop;
        this.windSpeed = windSpeed;
    }

    // getters/setters
    public Long getTsId() { return tsId; }
    public void setTsId(Long tsId) { this.tsId = tsId; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public LocalDateTime getForecastTime() { return forecastTime; }
    public void setForecastTime(LocalDateTime forecastTime) { this.forecastTime = forecastTime; }

    public Double getTemp() { return temp; }
    public void setTemp(Double temp) { this.temp = temp; }

    public Integer getHumidity() { return humidity; }
    public void setHumidity(Integer humidity) { this.humidity = humidity; }

    public Integer getPop() { return pop; }
    public void setPop(Integer pop) { this.pop = pop; }

    public Double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(Double windSpeed) { this.windSpeed = windSpeed; }
}
