package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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


    @GetMapping("/equipment")
    public Result<EquipmentTrendVO> getEquipmentTrend(TimeRange timeRange) {
        EquipmentTrendVO equipmentTrend = benchmarkService.getEquipmentTrend(timeRange.getSt(), timeRange.getEt());
        return Result.success(equipmentTrend);
    }

    @GetMapping("/trend")
    public Result<BenchmarkTrendVO> getTotalBenchmark(TimeRange timeRange) {
        BenchmarkTrendVO benchmarkTrend = benchmarkService.getBenchmarkTrend(timeRange.getSt(), timeRange.getEt());
        return Result.success(benchmarkTrend);
    }
}
