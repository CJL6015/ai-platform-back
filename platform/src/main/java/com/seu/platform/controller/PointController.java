package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.exa.ExaClient;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.*;
import com.seu.platform.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final ExaClient exaClient;

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
    @Cacheable("point-getInspectionTrendMonth")
    public Result<TrendVO<String, Integer>> getInspectionTrendMonth(@PathVariable Integer lineId,
                                                                    TimeRange timeRange) {
        TrendVO<String, Integer> pointInspectionTrendMonth = pointStatisticHourService.getPointInspectionTrendMonth(
                lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(pointInspectionTrendMonth);
    }

    @GetMapping("/inspection/trend/daily/line/{lineId}")
    @Cacheable("point-getInspectionTrendDaily")
    public Result<TrendVO<String, Integer>> getInspectionTrendDaily(@PathVariable Integer lineId,
                                                                    TimeRange timeRange) {
        TrendVO<String, Integer> pointInspectionTrendDaily = pointStatisticHourService.getPointInspectionTrendDaily(
                lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(pointInspectionTrendDaily);
    }

    @GetMapping("/benchmark/line/{lineId}")
    @Cacheable("point-getBenchmark")
    public Result<BenchmarkDataVO> getBenchmark(@PathVariable Integer lineId, Integer num) {
        BenchmarkDataVO benchmarkData = pointStatisticHourService.getBenchmarkData(lineId, num);
        return Result.success(benchmarkData);
    }

    @GetMapping("/trend/month/detail/line/{lineId}")
    @Cacheable("point-getTrendDetailMonth")
    public Result<TrendDetailVO> getTrendDetailMonth(@PathVariable Integer lineId) {
        TrendDetailVO trendDetailMonth = pointStatisticHourService.getTrendDetailMonth(lineId);
        return Result.success(trendDetailMonth);
    }

    @GetMapping("/trend/daily/detail/line/{lineId}")
    public Result<TrendDetailVO> getTrendDetailDaily(@PathVariable Integer lineId) {
        TrendDetailVO trendDetailMonth = pointStatisticHourService.getTrendDetailDaily(lineId);
        return Result.success(trendDetailMonth);
    }


    @GetMapping("/limits")
    public Result<Map<String, Double[]>> getPointLimit(@RequestParam String names) {
        Map<String, Double[]> pointLimits = pointCfgService.getPointLimits(names);
        return Result.success(pointLimits);
    }

    @PatchMapping("/")
    public Result<Boolean> updatePoint(@RequestBody PointConfigVO vo) {
        PointCfg pointCfg = BeanUtil.convertBean(vo, PointCfg.class);
        boolean b = pointCfgService.updateById(pointCfg);
        return Result.success(b);
    }

    @GetMapping("/status/{lineId}")
    public Result<List<StatusVO>> getPointsStatus(@PathVariable Integer lineId) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId);
        List<PointCfg> points = pointCfgService.list(queryWrapper);
        List<String> names = points.stream().map(t -> t.getName().trim()).collect(Collectors.toList());
        List<String> units = points.stream().map(t -> t.getUnit().trim()).collect(Collectors.toList());
        Boolean[] status = exaClient.getValuesBoolean(names);
        Boolean[] status1 = exaClient.getValuesBoolean(units);
        List<StatusVO> res = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            res.add(StatusVO.builder()
                    .name(points.get(i).getDescription().trim())
                    .status(status[i])
                    .warn(status1[i])
                    .point(names.get(i))
                    .build());
        }
        return Result.success(res);
    }

}
