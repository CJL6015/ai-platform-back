package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.LineStopRunStatisticHourMapper;
import com.seu.platform.dao.mapper.PointInspectionHourMapper;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.dto.CountIdDTO;
import com.seu.platform.model.dto.PointExceedInspectionDTO;
import com.seu.platform.model.dto.RunTimeDTO;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final PointInspectionHourMapper pointInspectionHourMapper;


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
        List<WarnCfg> list = warnCfgService.list();
        list.sort(Comparator.comparing(WarnCfg::getLineId));
        List<Double> scores = new ArrayList<>();
        List<CountIdDTO> lineScoreList = pointStatisticHourMapper.getLineScoreList(null, lastDay, st);
        Map<Integer, Double> lineScoreMap = lineScoreList.stream()
                .collect(Collectors.toMap(CountIdDTO::getId, CountIdDTO::getScore));
        List<PointExceedInspectionDTO> pointInspectionList = pointInspectionHourMapper.getPointInspectionList(null, lastDay, st);
        Map<Integer, PointExceedInspectionDTO> inspectionMap = pointInspectionList.stream()
                .collect(Collectors.toMap(PointExceedInspectionDTO::getLineId, t -> t));
        List<CountIdDTO> runHour = lineStopRunStatisticHourMapper.getRunHour(lastDay, st);
        Map<Integer, Double> runMap = runHour.stream().collect(Collectors.toMap(CountIdDTO::getId, CountIdDTO::getScore));
        for (int i = 1; i <= 4; i++) {
            Double v = runMap.get(i);
            if (v == null || Math.abs(v) < 0.01) {
                scores.add(100D);
                continue;
            }
            Double lineScore = lineScoreMap.getOrDefault(i, 0D);
            Integer exceedCount = processLinePictureHistMapper.getExceedCount(i, lastDay, st);
            WarnCfg warnCfg = list.get(i - 1);
            double peopleScore = warnCfg.getPeopleScore(exceedCount);
            PointExceedInspectionDTO pointInspection = inspectionMap.get(i);
            double inspectionScore = 0;
            if (pointInspection != null) {
                inspectionScore = Double.parseDouble(pointInspection.getScore(warnCfg.getScore(), warnCfg.getHighScore(), 1));
            }
            double s = 100 - lineScore - peopleScore - inspectionScore;
            scores.add(s);
        }
        scores.add(0, (scores.get(2) + scores.get(3)) / 2);
        scores.add(0, (scores.get(0) + scores.get(1)) / 2);
        return Result.success(scores);
    }
}
