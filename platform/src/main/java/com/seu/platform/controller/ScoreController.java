package com.seu.platform.controller;

import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.ScoreVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 18:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/score")
public class ScoreController {
    private final PointStatisticHourService pointStatisticHourService;

    private final ScoreService scoreService;

    @GetMapping("/{lineId}")
    @Cacheable("score-getScore")
    public Result<List<ScoreVO>> getScore(@PathVariable Integer lineId, TimeRange timeRange) {
        List<ScoreVO> scores = pointStatisticHourService.getScores(lineId, timeRange);
        return Result.success(scores);
    }

    @GetMapping("/trend/{lineId}")
    @Cacheable("score-getScoreTrend")
    public Result<List<List<Object>>> getScoreTrend(@PathVariable Integer lineId, TimeRange timeRange) {
        List<List<Object>> scoreTrend = scoreService.getScoreTrend(lineId, timeRange);
        return Result.success(scoreTrend);
    }

    @GetMapping("/summary/{lineId}")
    @Cacheable("score-getSummary")
    public Result<List<List<ScoreVO>>> getSummary(@PathVariable Integer lineId) {
        List<List<ScoreVO>> summary = pointStatisticHourService.getSummary(lineId);
        return Result.success(summary);
    }
}
