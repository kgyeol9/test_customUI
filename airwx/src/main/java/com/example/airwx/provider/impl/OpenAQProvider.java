package com.example.airwx.provider.impl;

import com.example.airwx.provider.AirProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OpenAQProvider implements AirProvider {

    private final RestClient http = RestClient.create();

    @Override
    public List<StationWithSamples> fetchLatestNear(double lat, double lon, int radiusMeters) {
        try {
            String url = "https://api.openaq.org/v2/latest"
                    + "?coordinates=" + lat + "," + lon
                    + "&radius=" + radiusMeters
                    + "&parameter=pm10,pm25,o3,no2,so2,co"
                    + "&limit=100";

            Map<?, ?> json = http.get().uri(url).retrieve().body(Map.class);
            if (json == null) throw new RuntimeException("OpenAQ 응답 null");

            List<Map<String, Object>> results = (List<Map<String, Object>>) json.get("results");
            if (results == null) results = List.of();

            List<StationWithSamples> out = new ArrayList<>();
            for (Map<String, Object> r : results) {
                String name = (String) r.get("location");
                Map<?, ?> coords = (Map<?, ?>) r.get("coordinates");
                Double slat = coords==null? null : asD(coords.get("latitude"));
                Double slon = coords==null? null : asD(coords.get("longitude"));
                String sido = Objects.toString(r.get("country"), ""); // 대체 필드

                List<Map<String,Object>> ms = (List<Map<String,Object>>) r.get("measurements");
                Map<LocalDateTime, Map<String,Number>> byT = new HashMap<>();
                if (ms != null) {
                    for (var m : ms) {
                        String parameter = (String) m.get("parameter");
                        String lastUpdated = (String) m.get("lastUpdated"); // ISO-8601 Z
                        LocalDateTime t = OffsetDateTime.parse(lastUpdated).toLocalDateTime();
                        byT.computeIfAbsent(t, k -> new HashMap<>()).put(parameter, (Number) m.get("value"));
                    }
                }
                List<AirSample> samples = byT.entrySet().stream().map(e -> {
                    Map<String, Number> m = e.getValue();
                    return new AirSample(
                            e.getKey(),
                            nI(m.get("pm10")), nI(m.get("pm25")),
                            nD(m.get("o3")), nD(m.get("no2")), nD(m.get("so2")), nD(m.get("co"))
                    );
                }).collect(Collectors.toList());

                out.add(new StationWithSamples(new AirPoint(name, sido, slat, slon), samples));
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("OpenAQ 호출/파싱 실패: " + e.getMessage(), e);
        }
    }

    private static Double asD(Object o){ return o==null? null : ((Number)o).doubleValue(); }
    private static Integer nI(Number n){ return n==null? null : n.intValue(); }
    private static Double nD(Number n){ return n==null? null : n.doubleValue(); }
}
