package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.LineStopRunStatisticHourMapper;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.WarnCfgService;
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

    private final ProcessLinePictureHistMapper processLinePictureHistMapper;

    private final WarnCfgService warnCfgService;


    @GetMapping("/score")
    public Result<String> getScore() {
        DateTime et = DateUtil.date();
        DateTime st = DateUtil.beginOfDay(et);
        DateTime dateTime = DateUtil.offsetDay(st, -1);
        Double lineScore = pointStatisticHourMapper.getLineScore(null, dateTime, st);
        Integer exceedCount = processLinePictureHistMapper.getExceedCount(null, dateTime, st);
        exceedCount = exceedCount == null ? 0 : exceedCount;
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, 1);
        WarnCfg one = warnCfgService.getOne(queryWrapper);
        Double peopleScore1 = one.getPeopleScore();
        peopleScore1 = peopleScore1 == null ? 2 : peopleScore1;
        double peopleScore = peopleScore1 * exceedCount;
        lineScore = 100 - lineScore / 4 - peopleScore / 4;
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
        Double lineScore1 = pointStatisticHourMapper.getLineScore(1, lastDay, st);
        Double lineScore2 = pointStatisticHourMapper.getLineScore(2, lastDay, st);
        Double lineScore3 = pointStatisticHourMapper.getLineScore(3, lastDay, st);
        Double lineScore4 = pointStatisticHourMapper.getLineScore(4, lastDay, st);
        Integer exceedCount1 = processLinePictureHistMapper.getExceedCount(1, lastDay, st);
        Integer exceedCount2 = processLinePictureHistMapper.getExceedCount(2, lastDay, st);
        Integer exceedCount3 = processLinePictureHistMapper.getExceedCount(3, lastDay, st);
        Integer exceedCount4 = processLinePictureHistMapper.getExceedCount(4, lastDay, st);
        lineScore1 = lineScore1 == null ? 0 : lineScore1;
        lineScore2 = lineScore2 == null ? 0 : lineScore1;
        lineScore3 = lineScore3 == null ? 0 : lineScore1;
        lineScore4 = lineScore4 == null ? 0 : lineScore1;
        exceedCount1 = exceedCount1 == null ? 0 : exceedCount1;
        exceedCount2 = exceedCount2 == null ? 0 : exceedCount2;
        exceedCount3 = exceedCount3 == null ? 0 : exceedCount3;
        exceedCount4 = exceedCount4 == null ? 0 : exceedCount4;
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, 1);
        WarnCfg one = warnCfgService.getOne(queryWrapper);
        Double peopleScore = one.getPeopleScore();
        peopleScore = peopleScore == null ? 2 : peopleScore;
        double score1 = 100 - lineScore1 - peopleScore * exceedCount1;
        double score2 = 100 - lineScore2 - peopleScore * exceedCount2;
        double score3 = 100 - lineScore3 - peopleScore * exceedCount3;
        double score4 = 100 - lineScore4 - peopleScore * exceedCount4;
        res.add((score1 + score2) / 2);
        res.add((score3 + score4) / 2);
        res.add(score1);
        res.add(score2);
        res.add(score3);
        res.add(score4);
        return Result.success(res);
    }
}
