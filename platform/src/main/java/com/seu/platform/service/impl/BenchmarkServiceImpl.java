package com.seu.platform.service.impl;

import com.seu.platform.dao.mapper.EquipmentCfgMapper;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.model.dto.EquipmentTrendDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.service.BenchmarkService;
import com.seu.platform.util.MathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:32
 */
@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private final EquipmentCfgMapper equipmentCfgMapper;

    private final PointStatisticHourMapper pointStatisticHourMapper;

    @Override
    public EquipmentTrendVO getEquipmentTrend(Date st, Date et) {
        List<EquipmentTrendDTO> equipmentTrend = equipmentCfgMapper.getEquipmentTrend(st, et);
        List<String> equipments = new ArrayList<>();
        List<List<Object[]>> data = new ArrayList<>();
        List<Object[]> value = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<Double> durations = new ArrayList<>();
        List<Integer> equipmentMax = new ArrayList<>();
        data.add(value);
        String name = equipmentTrend.get(0).getEquipmentName().trim();
        int count = 0;
        int max = 0;
        double duration = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (EquipmentTrendDTO dto : equipmentTrend) {
            String equipmentName = dto.getEquipmentName().trim();
            if (!equipments.contains(equipmentName)) {
                equipments.add(equipmentName);
            }
            if (!name.equals(equipmentName)) {
                value = new ArrayList<>();
                data.add(value);
                name = equipmentName;
                counts.add(count);
                count = 0;
                equipmentMax.add(max);
                max = 0;
                durations.add(duration);
                duration = 0;
            }
            Integer dtoCount = dto.getCount();
            max = Math.max(max, dtoCount);
            count += dtoCount;
            duration += dto.getDuration();
            value.add(new Object[]{dateFormat.format(dto.getTime()), dtoCount});
        }
        counts.add(count);
        equipmentMax.add(max);
        durations.add(duration);
        List<String> trend = new ArrayList<>();
        for (List<Object[]> list : data) {
            List<Integer> nums = new ArrayList<>();
            for (Object[] objects : list) {
                nums.add((int) objects[1]);
            }
            double a = MathUtil.fitting(nums, 1)[1];
            trend.add(a > 0 ? "上升趋势" : "下降趋势");
        }


        return EquipmentTrendVO.builder()
                .equipments(equipments)
                .equipmentMax(equipmentMax)
                .counts(counts)
                .trend(trend)
                .durations(durations)
                .values(data)
                .build();
    }


    @Override
    public BenchmarkTrendVO getBenchmarkTrend(Date st, Date et) {
        List<TrendDTO> totalTrend = pointStatisticHourMapper.getTotalTrend(st, et);
        List<Object[]> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : totalTrend) {
            trend.add(new Object[]{dateFormat.format(dto.getTime()), dto.getCount()});
        }

        List<HourTrendDTO> timeOverrun = pointStatisticHourMapper.getTimeOverrun(st, et);
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
}
