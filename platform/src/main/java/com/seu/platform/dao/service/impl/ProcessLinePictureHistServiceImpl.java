package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.DetectionResultVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.model.vo.TrendVO;
import com.seu.platform.util.MathUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    public List<ProcessLinePictureHist> getPendingChecks(int count) {
        return getBaseMapper().getPendingChecks(count);
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
}




