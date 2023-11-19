package com.seu.platform.dao.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointStatistic;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.dto.BenchmarkDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.*;
import com.seu.platform.util.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic_hour】的数据库操作Service实现
 * @createDate 2023-10-04 09:46:03
 */
@Service
@RequiredArgsConstructor
public class PointStatisticHourServiceImpl extends ServiceImpl<PointStatisticHourMapper, PointStatisticHour>
        implements PointStatisticHourService {

    private static List<Double> getPredictions(List<Integer> y, double[] parameters) {
        List<Double> predictions = new ArrayList<>();
        for (int i = 0; i < y.size(); i++) {
            double prediction = parameters[1] * i + parameters[0];
            predictions.add(prediction);
        }
        return predictions;
    }

    @Override
    public TrendVO<String, Integer> getPointInspectionTrendMonth(Integer lineId, Date st, Date et) {
        List<TrendDTO> overrunCountMonth = getBaseMapper().getOverrunCountMonth(lineId, st, et);
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : overrunCountMonth) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public TrendVO<String, Integer> getPointInspectionTrendDaily(Integer lineId, Date st, Date et) {
        List<TrendDTO> overrunCountDaily = getBaseMapper().getOverrunCountDaily(lineId, st, et);
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日HH时");
        for (TrendDTO dto : overrunCountDaily) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public BenchmarkDataVO getBenchmarkData(Integer lineId, Integer num) {
        List<BenchmarkDTO> benchmarkDaily = getBaseMapper().getBenchmarkDaily(lineId, -num);
        List<String> equipments = new ArrayList<>();
        List<List<Integer>> dayData = new ArrayList<>();
        if (CollUtil.isNotEmpty(benchmarkDaily)) {
            Date time = benchmarkDaily.get(0).getTime();
            List<Integer> counts = new ArrayList<>();
            dayData.add(counts);
            for (BenchmarkDTO benchmarkDTO : benchmarkDaily) {
                String equipmentName = benchmarkDTO.getEquipmentName().trim();
                if (!equipments.contains(equipmentName)) {
                    equipments.add(equipmentName);
                }
                Date dtoTime = benchmarkDTO.getTime();
                if (time.compareTo(dtoTime) != 0) {
                    counts = new ArrayList<>();
                    dayData.add(counts);
                    time = dtoTime;
                }
                counts.add(benchmarkDTO.getCount());
            }
        }


        return BenchmarkDataVO.builder()
                .equipments(equipments)
                .dayData(dayData)
                .build();
    }

    @Override
    public TrendDetailVO getTrendDetailMonth(Integer lineId) {
        List<BenchmarkDTO> trendDetailMonth = getBaseMapper().getTrendDetailMonth(lineId);
        List<String> equipments = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<List<Integer>> data = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        data.add(counts);
        Date time = trendDetailMonth.get(0).getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        times.add(dateFormat.format(time));
        for (BenchmarkDTO dto : trendDetailMonth) {
            String equipmentName = dto.getEquipmentName().trim();
            if (!equipments.contains(equipmentName)) {
                equipments.add(equipmentName);
            }
            Date dtoTime = dto.getTime();
            if (time.compareTo(dtoTime) != 0) {
                counts = new ArrayList<>();
                data.add(counts);
                time = dtoTime;
                times.add(dateFormat.format(time));
            }
            counts.add(dto.getCount());
        }
        return TrendDetailVO.builder()
                .data(data)
                .equipments(equipments)
                .times(times)
                .build();
    }

    @Override
    public TrendDetailVO getTrendDetailDaily(Integer lineId) {
        List<BenchmarkDTO> trendDetailMonth = getBaseMapper().getTrendDetailDaily(lineId);
        List<String> equipments = new ArrayList<>();
        List<String> times = new ArrayList<>();
        List<List<Integer>> data = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        data.add(counts);
        Date time = trendDetailMonth.get(0).getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH时");
        times.add(dateFormat.format(time));
        for (BenchmarkDTO dto : trendDetailMonth) {
            String equipmentName = dto.getEquipmentName().trim();
            if (!equipments.contains(equipmentName)) {
                equipments.add(equipmentName);
            }
            Date dtoTime = dto.getTime();
            if (time.compareTo(dtoTime) != 0) {
                counts = new ArrayList<>();
                data.add(counts);
                time = dtoTime;
                times.add(dateFormat.format(time));
            }
            counts.add(dto.getCount());
        }
        return TrendDetailVO.builder()
                .data(data)
                .equipments(equipments)
                .times(times)
                .build();
    }


    @Override
    public CompareVO getCompare(Integer id) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.plusDays(-1);
        LocalDate lastMonth = today.plusMonths(-1);
        LocalDate lastYear = today.plusYears(-1);
        List<String> days = new ArrayList<>();
        days.add(LocalDateTimeUtil.formatNormal(today));
        days.add(LocalDateTimeUtil.formatNormal(yesterday));
        days.add(LocalDateTimeUtil.formatNormal(lastMonth));
        days.add(LocalDateTimeUtil.formatNormal(lastYear));
        List<TrendDTO> dayCompare = getBaseMapper().getDayCompare(days);

        List<String> months = new ArrayList<>();
        months.add(LocalDateTimeUtil.format(today, DatePattern.NORM_MONTH_FORMATTER));
        months.add(LocalDateTimeUtil.format(lastMonth, DatePattern.NORM_MONTH_FORMATTER));
        months.add(LocalDateTimeUtil.format(lastYear, DatePattern.NORM_MONTH_FORMATTER));

        List<String> years = new ArrayList<>();
        years.add(LocalDateTimeUtil.format(today, DatePattern.NORM_YEAR_PATTERN));
        years.add(LocalDateTimeUtil.format(lastYear, DatePattern.NORM_YEAR_PATTERN));

        return null;
    }

    @Override
    public StatisticVO getStatistic(Integer lineId, TimeRange timeRange) {
        PointStatistic statistic = getBaseMapper().getStatistic(lineId,
                timeRange.getSt(),
                timeRange.getEt());
        statistic.setThresholdWithin(statistic.getNormalRefresh() - statistic.getThresholdExceeded());
        statistic.setNormalRefresh(statistic.getNormalRefresh() - statistic.getExceptionRefresh());
        return com.seu.platform.util.BeanUtil.convertBean(statistic, StatisticVO.class);
    }

    @Override
    public List<ScoreVO> getScores(Integer lineId, TimeRange timeRange) {
        List<ScoreVO> score = getBaseMapper().getScore(lineId, timeRange.getSt(), timeRange.getEt());
        score = score.stream().filter(Objects::nonNull).peek(t -> {
            String name = t.getName();
            if (StringUtils.hasText(name)) {
                t.setName(name.trim());
            }
        }).collect(Collectors.toList());
        return score;
    }


}




