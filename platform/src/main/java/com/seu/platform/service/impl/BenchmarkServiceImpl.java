package com.seu.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.CameraCfg;
import com.seu.platform.dao.entity.EquipmentCfg;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.mapper.*;
import com.seu.platform.model.dto.CountStatisticDTO;
import com.seu.platform.model.dto.EquipmentTrendDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.param.BenchmarkQuery;
import com.seu.platform.model.vo.AnalyzeVO;
import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.service.AnalyzeService;
import com.seu.platform.service.BenchmarkService;
import com.seu.platform.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private final EquipmentCfgMapper equipmentCfgMapper;

    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final ProcessLinePictureHistMapper processLinePictureHistMapper;

    private final CameraCfgMapper cameraCfgMapper;

    private final PointCfgMapper pointCfgMapper;

    private final AnalyzeService analyzeService;

    @Override
    public EquipmentTrendVO getEquipmentTrend(Integer lineId, Date st, Date et) {
        List<EquipmentTrendDTO> equipmentTrend = equipmentCfgMapper.getEquipmentTrend(lineId, st, et);
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
    public BenchmarkTrendVO getBenchmarkTrend(Integer lineId, Date st, Date et) {
        List<TrendDTO> totalTrend = pointStatisticHourMapper.getTotalTrend(lineId, st, et);
        List<Object[]> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : totalTrend) {
            trend.add(new Object[]{dateFormat.format(dto.getTime()), dto.getCount()});
        }

        List<HourTrendDTO> timeOverrun = pointStatisticHourMapper.getTimeOverrun(lineId, st, et);
        int[] hours = new int[24];
        for (HourTrendDTO dto : timeOverrun) {
            Integer time = dto.getTime();
            hours[time] = dto.getCount();
        }
        List<Integer> nums = totalTrend.stream().map(TrendDTO::getCount).collect(Collectors.toList());
        double a = MathUtil.fitting(nums, 1)[1];
        return BenchmarkTrendVO.builder()
                .trend(trend)
                .hours(hours)
                .summary(a > 0 ? "上升趋势" : "下降趋势")
                .build();
    }


    @Override
    public List<Map<String, Object>> getReport(BenchmarkQuery query) {
        log.info("生成对标报表:{}", JSON.toJSONString(query));
        List<Map<String, Object>> data = new ArrayList<>();

        //获取对标时间
        Date[] time = getTime(query.getTime());
        //获取生产线id
        List<Integer> lines = getLines(query);

        //总体指标
        addTotalIndicator(query, lines, data, time[0], time[1]);
        //分项指标
        addSumIndicator(query, lines, data, time[0], time[1]);
        //极值指标
        addExtremeIndicator(query, lines, data, time[0], time[1]);
        //趋势指标
        addTrendIndicator(query, lines, data, time[0], time[1]);
        //相关性指标
        log.info("对标报表结果:{}", data);
        return data;
    }


    public Date[] getTime(Integer time) {
        DateTime et = DateUtil.date();
        Date st;
        switch (time) {
            case 0:
                st = DateUtil.beginOfYear(et);
                break;
            case 1:
                st = DateUtil.beginOfQuarter(et);
                break;
            case 2:
                st = DateUtil.beginOfMonth(et);
                break;
            default:
                st = DateUtil.beginOfDay(et);
                break;
        }
        return new Date[]{st, et};
    }

    public List<Integer> getLines(BenchmarkQuery query) {
        List<Integer> lines = new ArrayList<>();
        List<Integer> line1 = query.getLine1();
        List<Integer> line2 = query.getLine2();
        List<Integer> line3 = query.getLine3();
        if (CollUtil.isNotEmpty(line1)) {
            lines.addAll(line1);
        }
        if (CollUtil.isNotEmpty(line2)) {
            lines.addAll(line2);
        }
        if (CollUtil.isNotEmpty(line3)) {
            lines.addAll(line3);
        }
        return lines;
    }

    public void addTrendIndicator(BenchmarkQuery query, List<Integer> lines, List<Map<String, Object>> data, Date st, Date et) {
        AnalyzeVO peopleAnalyze = analyzeService.getPeopleAnalyze(null, "-1");
        AnalyzeVO paramAnalyze = analyzeService.getParamAnalyze(null, -1);
        List<Integer> trend = query.getTrend();
        if (CollUtil.isNotEmpty(trend)) {
            for (Integer indicator : trend) {
                Map<String, Object> d = new HashMap<>(8);
                switch (indicator) {
                    case 0:
                        d.put("name", "超员同比、环比增长率");
                        for (Integer line : lines) {
                            Double monthOverMonth = peopleAnalyze.getMonthOverMonth();
                            Double monthOnMonth = peopleAnalyze.getMonthOnMonth();
                            monthOverMonth = monthOverMonth == null ? 0 : monthOverMonth;
                            monthOnMonth = monthOnMonth == null ? 0 : monthOnMonth;
                            String analyse = monthOverMonth + "%," + monthOnMonth + "%";
                            putValue(d, line, analyse);
                        }
                        break;
                    case 1:
                        d.put("name", "参数超限同比、环比增长率");
                        for (Integer line : lines) {
                            Double monthOverMonth = paramAnalyze.getMonthOverMonth();
                            Double monthOnMonth = paramAnalyze.getMonthOnMonth();
                            monthOverMonth = monthOverMonth == null ? 0 : monthOverMonth;
                            monthOnMonth = monthOnMonth == null ? 0 : monthOnMonth;
                            String analyse = monthOverMonth + "%," + monthOnMonth + "%";
                            putValue(d, line, analyse);
                        }
                        break;
                    case 2:
                        d.put("name", "超员次数变化趋势(升/降)");
                        for (Integer line : lines) {
                            Double monthOverMonth = peopleAnalyze.getMonthOverMonth();
                            monthOverMonth = monthOverMonth == null ? 0 : monthOverMonth;
                            String res = monthOverMonth > 0 ? "上升" : "下降";
                            putValue(d, line, res);
                        }
                        break;
                    default:
                        d.put("name", "参数超限次数变化趋势(升/降)");
                        for (Integer line : lines) {
                            Double monthOverMonth = paramAnalyze.getMonthOverMonth();
                            monthOverMonth = monthOverMonth == null ? 0 : monthOverMonth;
                            String res = monthOverMonth > 0 ? "上升" : "下降";
                            putValue(d, line, res);
                        }
                        break;
                }
                data.add(d);
            }
        }
    }

//    public String getPeopleRate(Integer lineId) {
//
//    }

    public void addExtremeIndicator(BenchmarkQuery query, List<Integer> lines, List<Map<String, Object>> data, Date st, Date et) {
        List<Integer> extremeIndicators = query.getExtremeIndicators();
        Integer num = query.getExtremeNumber();
        if (CollUtil.isNotEmpty(extremeIndicators)) {
            for (Integer indicator : extremeIndicators) {
                Map<String, Object> d = new HashMap<>(8);
                switch (indicator) {
                    case 0:
                        d.put("name", "超员人数最高的工序及次数");
                        for (Integer line : lines) {
                            String score = getTopProcess(line, st, et, num);
                            putValue(d, line, score);
                        }
                        break;
                    case 1:
                        d.put("name", "超限次数最高的设备及次数");
                        for (Integer line : lines) {
                            String score = getTopEquipment(line, st, et, num);
                            putValue(d, line, score);
                        }
                        break;
                    case 2:
                        d.put("name", "超限次数最高的传感器及次数");
                        for (Integer line : lines) {
                            String score = getTopPoint(line, st, et, num);
                            putValue(d, line, score);
                        }
                        break;
                    case 3:
                        d.put("name", "发生超员次数最高的时段及次数");
                        for (Integer line : lines) {
                            String score = getTopInspectionExceedTime(line, st, et, num);
                            putValue(d, line, score);
                        }
                        break;
                    default:
                        d.put("name", "参数超限次数最高的时段及次数");
                        for (Integer line : lines) {
                            String score = getTopParamExceedTime(line, st, et, num);
                            putValue(d, line, score);
                        }
                        break;
                }
                data.add(d);
            }
        }
    }

    public String getTopProcess(Integer lineId, Date st, Date et, int num) {
        List<CountStatisticDTO> top = processLinePictureHistMapper.getTopProcess(lineId, st, et);
        return top.stream()
                .limit(num)
                .map(t -> t.getName() + ":" + t.getCount() + "次")
                .collect(Collectors.joining(";"));
    }

    public String getTopEquipment(Integer lineId, Date st, Date et, int num) {
        List<CountStatisticDTO> top = pointStatisticHourMapper.getTopEquipment(lineId, st, et);
        return top.stream()
                .limit(num)
                .map(t -> t.getName() + ":" + t.getCount() + "次")
                .collect(Collectors.joining(";"));
    }

    public String getTopPoint(Integer lineId, Date st, Date et, int num) {
        List<CountStatisticDTO> top = pointStatisticHourMapper.getTopPoint(lineId, st, et);
        return top.stream()
                .limit(num)
                .map(t -> t.getName() + ":" + t.getCount() + "次")
                .collect(Collectors.joining(";"));
    }

    public String getTopParamExceedTime(Integer lineId, Date st, Date et, int num) {
        List<CountStatisticDTO> top = pointStatisticHourMapper.getTopTime(lineId, st, et);
        return top.stream()
                .limit(num)
                .map(t -> t.getName() + "时:" + t.getCount() + "次")
                .collect(Collectors.joining(";"));
    }

    public String getTopInspectionExceedTime(Integer lineId, Date st, Date et, int num) {
        List<CountStatisticDTO> top = processLinePictureHistMapper.getTopTime(lineId, st, et);
        return top.stream()
                .limit(num)
                .map(t -> t.getName() + "时:" + t.getCount() + "次")
                .collect(Collectors.joining(";"));
    }

    public void addSumIndicator(BenchmarkQuery query, List<Integer> lines, List<Map<String, Object>> data, Date st, Date et) {
        List<Integer> totalIndicators = query.getSubIndicators();
        if (CollUtil.isNotEmpty(totalIndicators)) {
            for (Integer indicator : totalIndicators) {
                Map<String, Object> d = new HashMap<>(8);
                switch (indicator) {
                    case 0:
                        d.put("name", "生产线平均超限次数");
                        for (Integer line : lines) {
                            Double score = 1.0 * getParamExceedCount(line, st, et);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.00", score));
                            putValue(d, line, score);
                        }
                        break;
                    case 1:
                        d.put("name", "生产线各工序平均超员次数");
                        for (Integer line : lines) {
                            Double score = 1.0 * getInspectionExceedCount(line, st, et) / getCameraCount(line);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.00", score));
                            putValue(d, line, score);
                        }
                        break;
                    case 2:
                        d.put("name", "生产线平均参数超限次数");
                        for (Integer line : lines) {
                            Double score = 1.0 * getParamExceedCount(line, st, et) / getPointCount(line);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.00", score));
                            putValue(d, line, score);
                        }
                        break;
                    default:
                        d.put("name", "生产线各设备超限次数");
                        for (Integer line : lines) {
                            Double score = 1.0 * getParamExceedCount(line, st, et) / getEquipmentCount(line);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.00", score));
                            putValue(d, line, score);
                        }
                        break;
                }
                data.add(d);
            }
        }
    }

    public Integer getPointCount(Integer lineId) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        return pointCfgMapper.selectCount(queryWrapper);
    }

    public Integer getCameraCount(Integer lineId) {
        LambdaQueryWrapper<CameraCfg> queryWrapper = new LambdaQueryWrapper<>();
        return cameraCfgMapper.selectCount(queryWrapper);
    }


    public Integer getEquipmentCount(Integer lineId) {
        LambdaQueryWrapper<EquipmentCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentCfg::getLineId, lineId);
        return equipmentCfgMapper.selectCount(queryWrapper);
    }

    public void addTotalIndicator(BenchmarkQuery query, List<Integer> lines, List<Map<String, Object>> data, Date st, Date et) {
        List<Integer> totalIndicators = query.getTotalIndicators();
        if (CollUtil.isNotEmpty(totalIndicators)) {
            for (Integer indicator : totalIndicators) {
                Map<String, Object> d = new HashMap<>(8);
                switch (indicator) {
                    case 0:
                        d.put("name", "生产线平均安全评分");
                        for (Integer line : lines) {
                            Double score1 = getParamExceedScore(line, st, et);
                            Double score2 = getInspectionExceedScore(line, st, et);
                            Double score = 100 - score1 - score2;
                            score = Double.valueOf(NumberUtil.decimalFormat("0.000", score));
                            putValue(d, line, score);
                        }
                        break;
                    case 1:
                        d.put("name", "超员平均扣分");
                        for (Integer line : lines) {
                            Double score = getInspectionExceedScore(line, st, et);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.000", score));
                            putValue(d, line, score);
                        }
                        break;
                    default:
                        d.put("name", "设备超限平均扣分");
                        for (Integer line : lines) {
                            Double score = getParamExceedScore(line, st, et);
                            score = Double.valueOf(NumberUtil.decimalFormat("0.000", score));
                            putValue(d, line, score);
                        }
                        break;
                }
                data.add(d);
            }
        }
    }

    public void putValue(Map<String, Object> data, Integer line, Object value) {
        data.put("P" + line, value);
    }

    public Integer getParamExceedCount(Integer lineId, Date st, Date et) {
        int day = (int) DateUtil.betweenDay(st, et, false);
        return pointStatisticHourMapper.getCountAvg(lineId, st, et) / day;
    }

    public Double getParamExceedScore(Integer lineId, Date st, Date et) {
        long day = DateUtil.betweenDay(st, et, false);
        return pointStatisticHourMapper.getLineScore(lineId, st, et) / day;
    }

    public Integer getInspectionExceedCount(Integer lineId, Date st, Date et) {
        int day = (int) DateUtil.betweenDay(st, et, false);
        Integer i = processLinePictureHistMapper.exceedCount(lineId, st, et);
        return i / day;
    }

    public Double getInspectionExceedScore(Integer lineId, Date st, Date et) {
        long day = DateUtil.betweenDay(st, et, false);
        Integer i = processLinePictureHistMapper.exceedCount(lineId, st, et);
        return (1.0 * i) / day;
    }
}
