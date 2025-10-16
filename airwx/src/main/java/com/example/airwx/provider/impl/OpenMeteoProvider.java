package com.example.airwx.provider.impl;

import com.example.airwx.provider.WeatherProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class OpenMeteoProvider implements WeatherProvider {

    private final RestClient http = RestClient.create();

    @Override
    public List<WxHourly> fetchHourly(double lat, double lon) {
        try {
            String url = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lon +
                    "&hourly=temperature_2m,relative_humidity_2m,precipitation_probability,wind_speed_10m" +
                    "&timezone=Asia%2FSeoul";

            Map<?, ?> json = http.get().uri(url).retrieve().body(Map.class);
            if (json == null || !json.containsKey("hourly")) {
                throw new RuntimeException("Open-Meteo 응답이 비어있음(hourly 없음)");
            }
            Map<?, ?> hourly = (Map<?, ?>) json.get("hourly");

            List<String> times = (List<String>) hourly.get("time");
            List<Number> temp = (List<Number>) hourly.get("temperature_2m");
            List<Number> hum  = (List<Number>) hourly.get("relative_humidity_2m");
            List<Number> pop  = (List<Number>) hourly.get("precipitation_probability");
            List<Number> wind = (List<Number>) hourly.get("wind_speed_10m");

            if (times == null) throw new RuntimeException("Open-Meteo 시간 배열 없음");

            List<WxHourly> out = new ArrayList<>();
            for (int i = 0; i < times.size(); i++) {
                out.add(new WxHourly(
                        LocalDateTime.parse(times.get(i)),
                        nD(temp, i), nI(hum, i), nI(pop, i), nD(wind, i)
                ));
            }
            return out;
        } catch (Exception e) {
            // 원인 파악이 쉽도록 구체 메시지로 래핑
            throw new RuntimeException("Open-Meteo 호출/파싱 실패: " + e.getClass().getSimpleName() + " - " + e.getMessage(), e);
        }
    }

    private static Double nD(List<Number> l, int i){ return (l==null||i>=l.size()||l.get(i)==null) ? null : l.get(i).doubleValue(); }
    private static Integer nI(List<Number> l, int i){ return (l==null||i>=l.size()||l.get(i)==null) ? null : l.get(i).intValue(); }
}
