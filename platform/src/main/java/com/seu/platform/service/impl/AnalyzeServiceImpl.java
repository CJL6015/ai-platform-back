package com.seu.platform.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.mapper.CameraCfgMapper;
import com.seu.platform.dao.mapper.EquipmentCfgMapper;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.model.vo.AnalyzeVO;
import com.seu.platform.model.vo.BenchmarkChartVO;
import com.seu.platform.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:10
 */
@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {
    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final ProcessLinePictureHistMapper processLinePictureHistMapper;

    private final CameraCfgMapper cameraCfgMapper;

    private final EquipmentCfgMapper equipmentCfgMapper;

    @Override
    public AnalyzeVO getParamAnalyze(Integer pointId) {
        //当前时间
        DateTime date = DateUtil.date();
        //当前
        DateTime monthBegin = DateUtil.beginOfMonth(date);
        DateTime quarterBegin = DateUtil.beginOfQuarter(date);
        DateTime yearBegin = DateUtil.beginOfYear(date);

        Integer month = pointStatisticHourMapper.getCount(pointId, monthBegin, date);
        Integer quarter = pointStatisticHourMapper.getCount(pointId, quarterBegin, date);
        Integer year = pointStatisticHourMapper.getCount(pointId, yearBegin, date);

        //上个月
        DateTime lastMonthBegin = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
        DateTime lastMonthEnd = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
        Integer lastMonth = pointStatisticHourMapper.getCount(pointId, lastMonthBegin, lastMonthEnd);

        //上个季度
        DateTime lastQuarterBegin = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
        DateTime lastQuarterEnd = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
        Integer lastQuarter = pointStatisticHourMapper.getCount(pointId, lastQuarterBegin, lastQuarterEnd);
        //去年
        DateTime lastYearToday = DateUtil.offsetMonth(DateUtil.date(), -12);
        DateTime lastYearMonthBegin = DateUtil.beginOfMonth(lastYearToday);
        DateTime lastYearMonthEnd = DateUtil.endOfMonth(lastYearToday);
        DateTime lastYearQuarterBegin = DateUtil.beginOfQuarter(lastYearToday);
        DateTime lastYearQuarterEnd = DateUtil.endOfQuarter(lastYearToday);
        DateTime lastYearBegin = DateUtil.beginOfYear(lastYearToday);
        DateTime lastYearEnd = DateUtil.endOfYear(lastYearToday);

        Integer lastYearMonth = pointStatisticHourMapper.getCount(pointId, lastYearMonthBegin, lastYearMonthEnd);
        Integer lastYearQuarter = pointStatisticHourMapper.getCount(pointId, lastYearQuarterBegin, lastYearQuarterEnd);
        Integer lastYear = pointStatisticHourMapper.getCount(pointId, lastYearBegin, lastYearEnd);


        return AnalyzeVO.builder().month(month).quarter(quarter).year(year).lastMonth(lastMonth).lastQuarter(lastQuarter).lastYearMonth(lastYearMonth).lastYearQuarter(lastYearQuarter).lastYear(lastYear).monthOnMonth(getRate(month, lastYearMonth)).monthOverMonth(getRate(month, lastMonth)).quarterOnQuarter(getRate(quarter, lastYearQuarter)).quarterOverQuarter(getRate(quarter, lastQuarter)).yearOverYear(getRate(year, lastYear)).build();
    }

    public Double getRate(Integer n1, Integer n2) {
        Double res = 100D;
        if (Objects.nonNull(n1) && Objects.nonNull(n2) && n2 != 0) {
            return res * n1 / n2;
        }
        return res;
    }

    @Override
    public AnalyzeVO getPeopleAnalyze(String cameraIp) {
        //当前时间
        DateTime date = DateUtil.date();
        //当前
        DateTime monthBegin = DateUtil.beginOfMonth(date);
        DateTime quarterBegin = DateUtil.beginOfQuarter(date);
        DateTime yearBegin = DateUtil.beginOfYear(date);

        Integer month = processLinePictureHistMapper.getCount(cameraIp, monthBegin, date);
        Integer quarter = processLinePictureHistMapper.getCount(cameraIp, quarterBegin, date);
        Integer year = processLinePictureHistMapper.getCount(cameraIp, yearBegin, date);

        //上个月
        DateTime lastMonthBegin = DateUtil.beginOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
        DateTime lastMonthEnd = DateUtil.endOfMonth(DateUtil.offsetMonth(DateUtil.date(), -1));
        Integer lastMonth = processLinePictureHistMapper.getCount(cameraIp, lastMonthBegin, lastMonthEnd);

        //上个季度
        DateTime lastQuarterBegin = DateUtil.beginOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
        DateTime lastQuarterEnd = DateUtil.endOfQuarter(DateUtil.offsetMonth(DateUtil.date(), -3));
        Integer lastQuarter = processLinePictureHistMapper.getCount(cameraIp, lastQuarterBegin, lastQuarterEnd);
        //去年
        DateTime lastYearToday = DateUtil.offsetMonth(DateUtil.date(), -12);
        DateTime lastYearMonthBegin = DateUtil.beginOfMonth(lastYearToday);
        DateTime lastYearMonthEnd = DateUtil.endOfMonth(lastYearToday);
        DateTime lastYearQuarterBegin = DateUtil.beginOfQuarter(lastYearToday);
        DateTime lastYearQuarterEnd = DateUtil.endOfQuarter(lastYearToday);
        DateTime lastYearBegin = DateUtil.beginOfYear(lastYearToday);
        DateTime lastYearEnd = DateUtil.endOfYear(lastYearToday);

        Integer lastYearMonth = processLinePictureHistMapper.getCount(cameraIp, lastYearMonthBegin, lastYearMonthEnd);
        Integer lastYearQuarter = processLinePictureHistMapper.getCount(cameraIp, lastYearQuarterBegin, lastYearQuarterEnd);
        Integer lastYear = processLinePictureHistMapper.getCount(cameraIp, lastYearBegin, lastYearEnd);
        return AnalyzeVO.builder().month(month).quarter(quarter).year(year).lastMonth(lastMonth).lastQuarter(lastQuarter).lastYearMonth(lastYearMonth).lastYearQuarter(lastYearQuarter).lastYear(lastYear).monthOnMonth(getRate(month, lastYearMonth)).monthOverMonth(getRate(month, lastMonth)).quarterOnQuarter(getRate(quarter, lastYearQuarter)).quarterOverQuarter(getRate(quarter, lastQuarter)).yearOverYear(getRate(year, lastYear)).build();
    }

    @Override
    public BenchmarkChartVO getPeopleBenchmarkDaily(Integer lineId, Integer num) {
        List<String> names = cameraCfgMapper.getNames(lineId);
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetDay(new Date(), -i);
            DateTime st = DateUtil.beginOfDay(et);
            List<Integer> counts = processLinePictureHistMapper.getCounts(lineId, st, et);

            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).rate(rate).max(max).build();
    }

    @Override
    public BenchmarkChartVO getPeopleBenchmarkMonth(Integer lineId, Integer num) {
        List<String> names = cameraCfgMapper.getNames(lineId);
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetMonth(new Date(), -i);
            DateTime st = DateUtil.beginOfMonth(et);
            List<Integer> counts = processLinePictureHistMapper.getCounts(lineId, st, DateUtil.endOfMonth(et));
            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).max(max).rate(rate).build();
    }

    @Override
    public BenchmarkChartVO getPeopleBenchmarkQuarter(Integer lineId, Integer num) {
        List<String> names = cameraCfgMapper.getNames(lineId);
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetMonth(new Date(), -3 * i);
            DateTime st = DateUtil.beginOfQuarter(et);
            List<Integer> counts = processLinePictureHistMapper.getCounts(lineId, st, DateUtil.endOfQuarter(et));
            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).max(max).rate(rate).build();
    }

    @Override
    public BenchmarkChartVO getPointBenchmarkDaily(Integer lineId, Integer num) {
        List<String> names = equipmentCfgMapper.getNames(lineId);
        names = names.stream().map(String::trim).collect(Collectors.toList());
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetDay(new Date(), -i);
            DateTime st = DateUtil.beginOfDay(et);
            List<Integer> counts = pointStatisticHourMapper.getCounts(lineId, st, et);
            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).max(max).rate(rate).build();
    }

    @Override
    public BenchmarkChartVO getPointBenchmarkMonth(Integer lineId, Integer num) {
        List<String> names = equipmentCfgMapper.getNames(lineId);
        names = names.stream().map(String::trim).collect(Collectors.toList());
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetMonth(new Date(), -i);
            DateTime st = DateUtil.beginOfMonth(et);
            List<Integer> counts = pointStatisticHourMapper.getCounts(lineId, st, DateUtil.endOfMonth(et));
            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).rate(rate).max(max).build();
    }

    @Override
    public BenchmarkChartVO getPointBenchmarkQuarter(Integer lineId, Integer num) {
        List<String> names = equipmentCfgMapper.getNames(lineId);
        names = names.stream().map(String::trim).collect(Collectors.toList());
        List<List<Integer>> values = new ArrayList<>();
        int max = 1;
        for (int i = 0; i < num; i++) {
            Date et = DateUtil.offsetMonth(new Date(), -3 * i);
            DateTime st = DateUtil.beginOfQuarter(et);
            List<Integer> counts = pointStatisticHourMapper.getCounts(lineId, st, DateUtil.endOfQuarter(et));
            max = Math.max(max, counts.stream().mapToInt(t -> t != null ? t : 0).max().getAsInt());
            values.add(counts);
        }
        int count = values.get(0).stream().mapToInt(t -> t != null ? t : 0).sum();
        int sum = 0;
        for (int i = 1; i < values.size(); i++) {
            sum += values.get(1).stream().mapToInt(t -> t != null ? t : 0).sum();
        }
        Double rate = getRate(count, sum);
        return BenchmarkChartVO.builder().names(names).values(values).rate(rate).max(max).build();
    }
}
