package com.seu.platform.controller;

import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.param.BenchmarkQuery;
import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.DetectionTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/benchmark")
public class BenchmarkController {
    private static Map<String, BenchmarkTrendVO> cache = new HashMap<>();
    private final BenchmarkService benchmarkService;
    private final ProcessLinePictureHistService processLinePictureHistService;

    @GetMapping("/detection/{lineId}")
    @Cacheable("benchmark-getPeopleTrend")
    public Result<DetectionTrendVO> getPeopleTrend(@PathVariable Integer lineId, TimeRange timeRange) {
        DetectionTrendVO peopleTrend = processLinePictureHistService.getPeopleTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(peopleTrend);
    }

    @GetMapping("/trend/detection/{lineId}")
    @Cacheable("benchmark-getPeopleTotalBenchmark")
    public Result<BenchmarkTrendVO> getPeopleTotalBenchmark(@PathVariable Integer lineId, TimeRange timeRange) {
        Date st = timeRange.getSt();
        Date et = timeRange.getEt();
        String st1 = DateUtil.format(st, "yyyy-MM-dd");
        String et1 = DateUtil.format(et, "yyyy-MM-dd");
        String key = st1 + et1 + lineId;
        BenchmarkTrendVO benchmarkTrendVO = cache.get(key);
        if (benchmarkTrendVO != null) {
            return Result.success(benchmarkTrendVO);
        }
        BenchmarkTrendVO benchmarkTrend = processLinePictureHistService.getBenchmarkTrend(lineId, st, et);
        cache.put(key, benchmarkTrend);
        return Result.success(benchmarkTrend);
    }


    @GetMapping("/equipment/{lineId}")
    @Cacheable("benchmark-getEquipmentTrend")
    public Result<EquipmentTrendVO> getEquipmentTrend(@PathVariable Integer lineId, TimeRange timeRange) {
        EquipmentTrendVO equipmentTrend = benchmarkService.getEquipmentTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(equipmentTrend);
    }

    @GetMapping("/trend/{lineId}")
    @Cacheable("benchmark-getTotalBenchmark")
    public Result<BenchmarkTrendVO> getTotalBenchmark(@PathVariable Integer lineId, TimeRange timeRange) {
        BenchmarkTrendVO benchmarkTrend = benchmarkService.getBenchmarkTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(benchmarkTrend);
    }


    @PostMapping("/report")
    @Cacheable("benchmark-getReport")
    public Result<List<Map<String, Object>>> getReport(@RequestBody BenchmarkQuery query) {
        List<Map<String, Object>> report = benchmarkService.getReport(query);
        return Result.success(report);
    }
}
