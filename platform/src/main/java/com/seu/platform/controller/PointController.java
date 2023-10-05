package com.seu.platform.controller;

import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 15:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {

    private final PointCfgService pointCfgService;

    private final PointStatisticHourService pointStatisticHourService;

    @GetMapping("/line/{lineId}")
    public Result<List<PointConfigVO>> getPointList(@PathVariable Integer lineId) {
        List<PointConfigVO> pointList = pointCfgService.getPointList(lineId);
        return Result.success(pointList);
    }

    @GetMapping("/trend/{name}")
    public Result<PointTrendVO> getPointTrend(@PathVariable String name,
                                              @RequestParam Long st,
                                              @RequestParam Long et)
            throws Exception {
        PointTrendVO pointTrend = pointCfgService.getPointTrend(name, st, et);
        return Result.success(pointTrend);
    }

    @GetMapping("/inspection/trend/month/line/{lineId}")
    public Result<TrendVO<String, Integer>> getInspectionTrendMonth(@PathVariable Integer lineId,
                                                                    TimeRange timeRange) {
        TrendVO<String, Integer> pointInspectionTrendMonth = pointStatisticHourService.getPointInspectionTrendMonth(
                lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(pointInspectionTrendMonth);
    }

    @GetMapping("/inspection/trend/daily/line/{lineId}")
    public Result<TrendVO<String, Integer>> getInspectionTrendDaily(@PathVariable Integer lineId,
                                                                    TimeRange timeRange) {
        TrendVO<String, Integer> pointInspectionTrendDaily = pointStatisticHourService.getPointInspectionTrendDaily(
                lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(pointInspectionTrendDaily);
    }

    @GetMapping("/benchmark/line/{lineId}")
    public Result<BenchmarkDataVO> getBenchmark(@PathVariable Integer lineId) {
        BenchmarkDataVO benchmarkData = pointStatisticHourService.getBenchmarkData(lineId);
        return Result.success(benchmarkData);
    }

    @GetMapping("/trend/month/detail/line/{lineId}")
    public Result<TrendDetailVO> getTrendDetailMonth(@PathVariable Integer lineId) {
        TrendDetailVO trendDetailMonth = pointStatisticHourService.getTrendDetailMonth(lineId);
        return Result.success(trendDetailMonth);
    }

    @GetMapping("/trend/daily/detail/line/{lineId}")
    public Result<TrendDetailVO> getTrendDetailDaily(@PathVariable Integer lineId) {
        TrendDetailVO trendDetailMonth = pointStatisticHourService.getTrendDetailDaily(lineId);
        return Result.success(trendDetailMonth);
    }

//    @GetMapping("/compare/{id}")
//    public Result<CompareVO> getCompare(@PathVariable Integer id) {
//
//    }

}
