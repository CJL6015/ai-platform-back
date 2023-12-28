package com.seu.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.ScoreDailyVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.service.ScoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 19:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {
    private final WarnCfgService warnCfgService;
    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final ProcessLinePictureHistMapper processLinePictureHistMapper;

    public static List<String> generateDateRange(Date startDate, Date endDate) {
        List<String> dateList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startDate);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(endDate);

            while (!startCalendar.after(endCalendar)) {
                Date result = startCalendar.getTime();
                dateList.add(sdf.format(result));
                startCalendar.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
            log.error("生成时间异常", e);
        }

        return dateList;
    }

    @Override
    public List<List<Object>> getScoreTrend(Integer lineId, TimeRange timeRange) {
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, lineId);
        WarnCfg one = warnCfgService.getOne(queryWrapper);
        Double peopleScore = one.getPeopleScore();
        List<ScoreDailyVO> scoreDaily = pointStatisticHourMapper.getScoreDaily(lineId,
                timeRange.getSt(), timeRange.getEt());
        List<TrendDTO> scoreDaily1 = processLinePictureHistMapper.getScoreDaily(lineId,
                timeRange.getSt(), timeRange.getEt());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dates = generateDateRange(timeRange.getSt(), timeRange.getEt());
        List<Object> score1 = new ArrayList<>();
        List<Object> score2 = new ArrayList<>();
        List<Object> total = new ArrayList<>();
        for (String date : dates) {
            Optional<ScoreDailyVO> first = scoreDaily.stream().filter(t -> date.equals(dateFormat.format(t.getTime()))).findFirst();
            double s = 0D;
            if (first.isPresent()) {
                s = first.get().getScore();
            }
            score1.add(s);
            Optional<TrendDTO> first1 = scoreDaily1.stream().filter(t -> date.equals(dateFormat.format(t.getTime()))).findFirst();
            double s2 = 0D;
            if (first1.isPresent()) {
                s2 = peopleScore * (first1.get().getCount());
            }
            score2.add(s2);
            total.add(100 - s - s2);
        }
        List<List<Object>> result = new ArrayList<>();
        result.add(dates.stream().map(t -> (Object) t).collect(Collectors.toList()));
        result.add(score1);
        result.add(score2);
        result.add(total);

        return result;
    }

}
