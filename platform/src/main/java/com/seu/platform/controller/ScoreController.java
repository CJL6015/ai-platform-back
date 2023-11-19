package com.seu.platform.controller;

import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.ScoreVO;
import com.seu.platform.model.vo.TimeRange;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{lineId}")
    public Result<List<ScoreVO>> getScore(@PathVariable Integer lineId, TimeRange timeRange) {
        List<ScoreVO> scores = pointStatisticHourService.getScores(lineId, timeRange);
        return Result.success(scores);
    }
}
