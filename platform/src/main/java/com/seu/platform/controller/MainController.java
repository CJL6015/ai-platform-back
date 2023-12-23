package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.seu.platform.dao.mapper.LineStopRunStatisticHourMapper;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.model.dto.RunTimeDTO;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-23 13:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {
    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final LineStopRunStatisticHourMapper lineStopRunStatisticHourMapper;


    @GetMapping("/score")
    public Result<String> getScore() {
        DateTime et = DateUtil.date();
        DateTime st = DateUtil.beginOfDay(et);
        DateTime dateTime = DateUtil.offsetDay(st, -1);
        Double lineScore = pointStatisticHourMapper.getLineScore(null, dateTime, st);
        lineScore = 100 - lineScore;
        String s = NumberUtil.decimalFormat("#0.00", lineScore);
        return Result.success(s);
    }

    @GetMapping("/ratio")
    public Result<RunTimeDTO> getRatio() {
        DateTime et = DateUtil.date();
        DateTime st = DateUtil.beginOfDay(et);
        DateTime dateTime = DateUtil.offsetDay(st, -1);
        RunTimeDTO runTime = lineStopRunStatisticHourMapper.getRunTime(dateTime, st);
        return Result.success(runTime);
    }


    @GetMapping("/trend")
    public Result<List<Double>> getTrend() {
        DateTime et = DateUtil.date();
        DateTime st = DateUtil.beginOfDay(et);
        DateTime lastDay = DateUtil.offsetDay(st, -1);
        List<Double> res = new ArrayList<>();
        Double plantScore1 = pointStatisticHourMapper.getPlantScore(2, lastDay, st);
        Double plantScore2 = pointStatisticHourMapper.getPlantScore(3, lastDay, st);
        Double lineScore1 = pointStatisticHourMapper.getLineScore(1, lastDay, st);
        Double lineScore2 = pointStatisticHourMapper.getLineScore(2, lastDay, st);
        Double lineScore3 = pointStatisticHourMapper.getLineScore(3, lastDay, st);
        Double lineScore4 = pointStatisticHourMapper.getLineScore(4, lastDay, st);
        lineScore1 = lineScore1 == null ? 0 : lineScore1;
        lineScore2 = lineScore2 == null ? 0 : lineScore1;
        lineScore3 = lineScore3 == null ? 0 : lineScore1;
        lineScore4 = lineScore4 == null ? 0 : lineScore1;
        res.add(100 - plantScore1);
        res.add(100 - plantScore2);
        res.add(100 - lineScore1);
        res.add(100 - lineScore2);
        res.add(100 - lineScore3);
        res.add(100 - lineScore4);
        return Result.success(res);
    }
}
