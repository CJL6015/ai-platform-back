package com.seu.platform.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.model.vo.ParamAnalyzeVO;
import com.seu.platform.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:10
 */
@Service
@RequiredArgsConstructor
public class AnalyzeServiceImpl implements AnalyzeService {
    private final PointStatisticHourMapper pointStatisticHourMapper;

    @Override
    public ParamAnalyzeVO getParamAnalyze(Integer pointId) {
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


        return ParamAnalyzeVO.builder()
                .month(month)
                .quarter(quarter)
                .year(year)
                .lastMonth(lastMonth)
                .lastQuarter(lastQuarter)
                .lastYearMonth(lastYearMonth)
                .lastYearQuarter(lastYearQuarter)
                .lastYear(lastYear)
                .build();
    }
}
