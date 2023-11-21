package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.ProcessLinePictureHist1;
import com.seu.platform.dao.mapper.ProcessLinePictureHist1Mapper;
import com.seu.platform.dao.service.ProcessLinePictureHist1Service;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.entity.LineInspection;
import com.seu.platform.model.vo.DetectionResultVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.model.vo.TrendVO;
import com.seu.platform.util.MathUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist_1】的数据库操作Service实现
 * @createDate 2023-10-30 23:54:52
 */
@Service
public class ProcessLinePictureHist1ServiceImpl extends ServiceImpl<ProcessLinePictureHist1Mapper, ProcessLinePictureHist1>
        implements ProcessLinePictureHist1Service {
    private static List<Double> getPredictions(List<Integer> y, double[] parameters) {
        List<Double> predictions = new ArrayList<>();
        for (int i = 0; i < y.size(); i++) {
            double prediction = parameters[1] * i + parameters[0];
            predictions.add(prediction);
        }
        return predictions;
    }

    @Override
    public List<ProcessLinePictureHist1> getPendingChecks(Integer count, Set<Long> ids) {
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
    public List<LineInspection> getLast() {
        return getBaseMapper().getLast();
    }
}




