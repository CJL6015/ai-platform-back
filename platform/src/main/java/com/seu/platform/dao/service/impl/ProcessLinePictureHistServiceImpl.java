package com.seu.platform.dao.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.dto.DetectionTrendDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.entity.LineInspection;
import com.seu.platform.model.vo.*;
import com.seu.platform.util.MathUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist】的数据库操作Service实现
 * @createDate 2023-10-28 10:48:15
 */
@Service
public class ProcessLinePictureHistServiceImpl extends ServiceImpl<ProcessLinePictureHistMapper, ProcessLinePictureHist>
        implements ProcessLinePictureHistService {

    private static List<Double> getPredictions(List<Integer> y, double[] parameters) {
        List<Double> predictions = new ArrayList<>();
        for (int i = 0; i < y.size(); i++) {
            double prediction = parameters[1] * i + parameters[0];
            predictions.add(prediction);
        }
        return predictions;
    }

    @Override
    public List<ProcessLinePictureHist> getPendingChecks(Integer count, Set<Long> ids) {
        return getBaseMapper().getPendingChecks(count, ids);
    }

    @Override
    public List<DetectionResultVO> getDetectionResult(List<String> ips, Date time) {
        if (Objects.isNull(time)) {
            time = getBaseMapper().lastTime();
        }
        return getBaseMapper().getDetectionResult(ips, time);
    }

    @Override
    public TrendVO<String, Integer> getTrendMonth(TimeRange timeRange) {
        List<TrendDTO> monthTrend = getBaseMapper().getMonthTrend(timeRange.getSt(), timeRange.getEt());
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : monthTrend) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public TrendVO<String, Integer> getTrendDaily(TimeRange timeRange) {
        List<TrendDTO> dailyTrend = getBaseMapper().getDailyTrend(timeRange.getSt(), timeRange.getEt());
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日HH时");
        for (TrendDTO dto : dailyTrend) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public DetectionTrendVO getPeopleTrend(Integer lineId, Date st, Date et) {
        List<DetectionTrendDTO> peopleTrend = getBaseMapper().getPeopleTrend(lineId, st, et);
        List<String> names = new ArrayList<>();
        List<List<Object[]>> data = new ArrayList<>();
        List<Object[]> value = new ArrayList<>();
        List<Integer> peopleMax = new ArrayList<>();
        List<Integer> countMax = new ArrayList<>();
        data.add(value);
        List<Integer> counts = new ArrayList<>();
        List<String> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (CollUtil.isNotEmpty(peopleTrend)) {
            String name = peopleTrend.get(0).getName().trim();
            int count = 0;
            int max = 0;
            int maxPeople = 0;
            for (DetectionTrendDTO dto : peopleTrend) {
                String processName = dto.getName().trim();
                if (!names.contains(processName)) {
                    names.add(processName);
                }
                if (!name.equals(processName)) {
                    value = new ArrayList<>();
                    data.add(value);
                    name = processName;
                    counts.add(count);
                    count = 0;
                    countMax.add(max);
                    max = 0;
                    peopleMax.add(maxPeople);
                    maxPeople = 0;
                }
                Integer dtoCount = dto.getCount();
                max = Math.max(max, dtoCount);
                Integer maxCount = dto.getMaxCount();
                maxPeople = Math.max(maxCount, maxPeople);
                count += dtoCount;
                value.add(new Object[]{dateFormat.format(dto.getTime()), dtoCount});
            }
            counts.add(count);
            countMax.add(max);
            peopleMax.add(maxPeople);
            for (List<Object[]> list : data) {
                List<Integer> nums = new ArrayList<>();
                for (Object[] objects : list) {
                    nums.add((int) objects[1]);
                }
                double a = MathUtil.fitting(nums, 1)[1];
                trend.add(a > 0 ? "上升趋势" : "下降趋势");
            }
        }

        return DetectionTrendVO.builder()
                .values(data)
                .names(names)
                .peopleMax(peopleMax)
                .countMax(countMax)
                .counts(counts)
                .trend(trend)
                .build();
    }

    @Override
    public BenchmarkTrendVO getBenchmarkTrend(Integer lineId, Date st, Date et) {
        List<TrendDTO> totalTrend = getBaseMapper().getTotalTrend(lineId, st, et);
        List<Object[]> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : totalTrend) {
            trend.add(new Object[]{dateFormat.format(dto.getTime()), dto.getCount()});
        }

        List<HourTrendDTO> timeOverrun = getBaseMapper().getTimeOverrun(lineId, st, et);
        int[] hours = new int[24];
        for (HourTrendDTO dto : timeOverrun) {
            Integer time = dto.getTime();
            hours[time] = dto.getCount();
        }

        return BenchmarkTrendVO.builder()
                .trend(trend)
                .hours(hours)
                .build();
    }

    @Override
    public List<LineInspection> getLast() {
        return getBaseMapper().getLast();
    }

    @Override
    public Integer exceedCount(Integer lineId, Date st, Date et) {
        return getBaseMapper().exceedCount(lineId, st, et);
    }

    @Override
    public Boolean setInspectionMinute(String cameraIp, Date st, Date et) {
        return getBaseMapper().setInspectionMinute(cameraIp, st, et);
    }

    @Override
    public Date getFirstTime(String cameraIp) {
        return getBaseMapper().getFirstTime(cameraIp);
    }
}




