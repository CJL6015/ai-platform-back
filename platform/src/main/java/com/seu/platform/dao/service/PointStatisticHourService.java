package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.model.vo.*;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic_hour】的数据库操作Service
 * @createDate 2023-10-04 09:46:03
 */
public interface PointStatisticHourService extends IService<PointStatisticHour> {
    /**
     * 查询超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 超限趋势
     */
    TrendVO<String, Integer> getPointInspectionTrendMonth(Integer lineId, Date st, Date et);


    /**
     * 查询超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 超限趋势
     */
    TrendVO<String, Integer> getPointInspectionTrendDaily(Integer lineId, Date st, Date et);

    /**
     * 获取对标数据
     *
     * @param lineId 生产线id
     * @param num    对比个数
     * @return 数据
     */
    BenchmarkDataVO getBenchmarkData(Integer lineId, Integer num);

    /**
     * 获取趋势详情
     *
     * @param lineId 生产线id
     * @return 数据
     */
    TrendDetailVO getTrendDetailMonth(Integer lineId);

    /**
     * 获取趋势详情
     *
     * @param lineId 生产线id
     * @return 数据
     */
    TrendDetailVO getTrendDetailDaily(Integer lineId);

    /**
     * 获取同比环比
     *
     * @param id point id
     * @return 数据
     */
    CompareVO getCompare(Integer id);

    /**
     * 获取时间段内的统计结果
     *
     * @param lineId    生产线id
     * @param timeRange 时间范围
     * @return 统计结果
     */
    StatisticVO getStatistic(Integer lineId, TimeRange timeRange);

    List<ScoreVO> getScores(Integer lineId, TimeRange timeRange);

    List<List<ScoreVO>> getSummary(Integer lineId);

}
