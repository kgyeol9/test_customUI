package com.example.airwx.provider;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherProvider {
    final class WxHourly {
        private final LocalDateTime forecastTime;
        private final Double temp;
        private final Integer humidity;
        private final Integer pop;
        private final Double windSpeed;

        public WxHourly(LocalDateTime forecastTime, Double temp, Integer humidity, Integer pop, Double windSpeed) {
            this.forecastTime = forecastTime;
            this.temp = temp;
            this.humidity = humidity;
            this.pop = pop;
            this.windSpeed = windSpeed;
        }
        public LocalDateTime getForecastTime() { return forecastTime; }
        public Double getTemp() { return temp; }
        public Integer getHumidity() { return humidity; }
        public Integer getPop() { return pop; }
        public Double getWindSpeed() { return windSpeed; }
    }

    List<WxHourly> fetchHourly(double lat, double lon);
}
