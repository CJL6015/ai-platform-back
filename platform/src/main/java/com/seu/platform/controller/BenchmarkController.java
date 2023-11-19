package com.seu.platform.controller;

import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.DetectionTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/benchmark")
public class BenchmarkController {
    private final BenchmarkService benchmarkService;

    private final ProcessLinePictureHistService processLinePictureHistService;

    @GetMapping("/detection/{lineId}")
    public Result<DetectionTrendVO> getPeopleTrend(@PathVariable Integer lineId, TimeRange timeRange) {
        DetectionTrendVO peopleTrend = processLinePictureHistService.getPeopleTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(peopleTrend);
    }

    @GetMapping("/trend/detection/{lineId}")
    public Result<BenchmarkTrendVO> getPeopleTotalBenchmark(@PathVariable Integer lineId, TimeRange timeRange) {
        BenchmarkTrendVO benchmarkTrend = processLinePictureHistService.getBenchmarkTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(benchmarkTrend);
    }


    @GetMapping("/equipment/{lineId}")
    public Result<EquipmentTrendVO> getEquipmentTrend(@PathVariable Integer lineId, TimeRange timeRange) {
        EquipmentTrendVO equipmentTrend = benchmarkService.getEquipmentTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(equipmentTrend);
    }

    @GetMapping("/trend/{lineId}")
    public Result<BenchmarkTrendVO> getTotalBenchmark(@PathVariable Integer lineId, TimeRange timeRange) {
        BenchmarkTrendVO benchmarkTrend = benchmarkService.getBenchmarkTrend(lineId, timeRange.getSt(), timeRange.getEt());
        return Result.success(benchmarkTrend);
    }
}
