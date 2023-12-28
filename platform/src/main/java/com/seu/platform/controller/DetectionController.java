package com.seu.platform.controller;

import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.DetectionResultVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.model.vo.TrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-28 15:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detection")
public class DetectionController {

    private final ProcessLinePictureHistService processLinePictureHistService;

    @GetMapping("/times/{lineId}")
    public Result<List<String>> getDetectionTimes(@PathVariable Integer lineId) {
        List<String> times = processLinePictureHistService.getTimes(lineId);
        return Result.success(times);
    }

    @GetMapping("/result/{lineId}")
    public Result<List<DetectionResultVO>> getDetectionResult(@PathVariable Integer lineId,
                                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                              Date time) {
        List<DetectionResultVO> detectionResult = processLinePictureHistService.getDetectionResult(lineId, time);
        return Result.success(detectionResult);
    }

    @GetMapping("/trend/month/{lineId}")
    public Result<TrendVO<String, Integer>> getTrendMonth(@PathVariable Integer lineId,TimeRange timeRange) {
        TrendVO<String, Integer> trendMonth = processLinePictureHistService.getTrendMonth(lineId,timeRange);
        return Result.success(trendMonth);
    }

    @GetMapping("/trend/daily/{lineId}")
    public Result<TrendVO<String, Integer>> getTrendDaily(@PathVariable Integer lineId,TimeRange timeRange) {
        TrendVO<String, Integer> trendMonth = processLinePictureHistService.getTrendDaily(lineId,timeRange);
        return Result.success(trendMonth);
    }
}
