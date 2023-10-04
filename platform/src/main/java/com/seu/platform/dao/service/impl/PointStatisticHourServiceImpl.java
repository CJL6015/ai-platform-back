package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.dto.BenchmarkDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.BenchmarkDataVO;
import com.seu.platform.model.vo.TrendDetailVO;
import com.seu.platform.model.vo.TrendVO;
import com.seu.platform.util.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public BenchmarkDataVO getBenchmarkData(Integer lineId) {
        List<BenchmarkDTO> benchmarkDaily = getBaseMapper().getBenchmarkDaily(lineId);
        List<String> equipments = new ArrayList<>();
        Date time = benchmarkDaily.get(0).getTime();
        List<List<Integer>> dayData = new ArrayList<>();
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
}




