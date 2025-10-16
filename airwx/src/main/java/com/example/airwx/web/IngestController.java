package com.example.airwx.web;

import com.example.airwx.repo.AqTimeseriesRepository;
import com.example.airwx.repo.WxTimeseriesRepository;
import com.example.airwx.service.EtlService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class IngestController {

    private final EtlService etlService;
    private final WxTimeseriesRepository wxRepo;
    private final AqTimeseriesRepository aqRepo;

    public IngestController(EtlService etlService,
                            WxTimeseriesRepository wxRepo,
                            AqTimeseriesRepository aqRepo) {
        this.etlService = etlService;
        this.wxRepo = wxRepo;
        this.aqRepo = aqRepo;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("wxCount", wxRepo.count());
        model.addAttribute("aqCount", aqRepo.count());
        return "index";
    }

    @RequestMapping(value="/ingest/weather", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> ingestWeather(@RequestParam(name="mock", required=false) Integer mock) {
        Map<String,Object> res = new HashMap<>();
        long before = wxRepo.count();
        res.put("before", before);
        try {
            boolean usedMock = (mock != null && mock == 1);
            int inserted = etlService.ingestWeatherWithResult(36.333, 127.433, "대전광역시", "동구", usedMock);
            long after = wxRepo.count();
            res.put("inserted", inserted);
            res.put("after", after);
            res.put("ok", true);
            res.put("usedMock", usedMock);
        } catch (Exception e) {
            res.put("ok", false);
            res.put("error", e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    @RequestMapping(value="/ingest/air", method={RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> ingestAir(@RequestParam(name="mock", required=false) Integer mock) {
        Map<String,Object> res = new HashMap<>();
        long before = aqRepo.count();
        res.put("before", before);
        try {
            boolean usedMock = (mock != null && mock == 1);
            int inserted = etlService.ingestAirWithResult(36.333, 127.433, "대전", usedMock);
            long after = aqRepo.count();
            res.put("inserted", inserted);
            res.put("after", after);
            res.put("ok", true);
            res.put("usedMock", usedMock);
        } catch (Exception e) {
            res.put("ok", false);
            res.put("error", e.getClass().getSimpleName()+": "+e.getMessage());
            e.printStackTrace();
        }
        return res;
    }
}
