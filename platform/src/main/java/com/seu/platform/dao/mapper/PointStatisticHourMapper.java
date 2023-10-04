package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.model.dto.BenchmarkDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic_hour】的数据库操作Mapper
 * @createDate 2023-10-04 09:46:03
 * @Entity com.seu.platform.dao.entity.PointStatisticHour
 */
public interface PointStatisticHourMapper extends BaseMapper<PointStatisticHour> {
    /**
     * 统计超限月趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 统计结果
     */
    List<TrendDTO> getOverrunCountMonth(Integer lineId, Date st, Date et);


    /**
     * 统计超限日趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 统计结果
     */
    List<TrendDTO> getOverrunCountDaily(Integer lineId, Date st, Date et);

    /**
     * 对标
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<BenchmarkDTO> getBenchmarkDaily(Integer lineId);

    /**
     * 对标
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<BenchmarkDTO> getBenchmarkMonth(Integer lineId);

    /**
     * 趋势
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<BenchmarkDTO> getTrendDetailMonth(Integer lineId);


    /**
     * 趋势
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<BenchmarkDTO> getTrendDetailDaily(Integer lineId);

    /**
     * 获取总趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 趋势
     */
    List<TrendDTO> getTotalTrend(Date st, Date et);

    /**
     * 获取总趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 趋势
     */
    List<HourTrendDTO> getTimeOverrun(Date st, Date et);
}




