package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.AnalyzeVO;
import com.seu.platform.model.vo.BenchmarkChartVO;
import com.seu.platform.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:08
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyze")
public class AnalyzeController {
    private final AnalyzeService analyzeService;

    @GetMapping("/param/{lineId}")
    public Result<AnalyzeVO> getParamAnalyze(@PathVariable Integer lineId, Integer pointId) {
        AnalyzeVO paramAnalyze = analyzeService.getParamAnalyze(lineId,pointId);
        return Result.success(paramAnalyze);
    }

    @GetMapping("/inspection/{lineId}")
    public Result<AnalyzeVO> getPeopleAnalyze(@PathVariable Integer lineId, String cameraIp) {
        AnalyzeVO peopleAnalyze = analyzeService.getPeopleAnalyze(lineId, cameraIp);
        return Result.success(peopleAnalyze);
    }

    @GetMapping("/inspection/daily/{lineId}")
    public Result<BenchmarkChartVO> getPeopleDaily(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPeopleBenchmarkDaily(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }

    @GetMapping("/inspection/month/{lineId}")
    public Result<BenchmarkChartVO> getPeopleMonth(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPeopleBenchmarkMonth(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }

    @GetMapping("/inspection/quarter/{lineId}")
    public Result<BenchmarkChartVO> getPeopleQuarter(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPeopleBenchmarkQuarter(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }


    @GetMapping("/param/daily/{lineId}")
    public Result<BenchmarkChartVO> getParamDaily(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPointBenchmarkDaily(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }

    @GetMapping("/param/month/{lineId}")
    public Result<BenchmarkChartVO> getParamMonth(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPointBenchmarkMonth(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }

    @GetMapping("/param/quarter/{lineId}")
    public Result<BenchmarkChartVO> getParamQuarter(@PathVariable Integer lineId, Integer num) {
        BenchmarkChartVO peopleBenchmarkDaily = analyzeService.getPointBenchmarkQuarter(lineId, num);
        return Result.success(peopleBenchmarkDaily);
    }

}
