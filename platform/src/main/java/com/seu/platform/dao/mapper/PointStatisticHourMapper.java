package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.PointStatistic;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.model.dto.*;
import com.seu.platform.model.vo.ScoreDailyVO;
import com.seu.platform.model.vo.ScoreVO;

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
     * @param num    对比个数
     * @return 数据
     */
    List<BenchmarkDTO> getBenchmarkDaily(Integer lineId, Integer num);

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
    List<BenchmarkDTO> getTrendDetailMonth(Integer lineId, Date time);


    /**
     * 趋势
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<BenchmarkDTO> getTrendDetailDaily(Integer lineId, Date time);

    /**
     * 获取总趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    List<TrendDTO> getTotalTrend(Integer lineId, Date st, Date et);

    /**
     * 获取总趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    List<HourTrendDTO> getTimeOverrun(Integer lineId, Date st, Date et);

    List<CountStatisticDTO> getTopTime(Integer lineId, Date st, Date et);

    /**
     * 获取日同比
     *
     * @param times 时间
     * @return 数据
     */
    List<TrendDTO> getDayCompare(List<String> times);

    /**
     * 获取统计结果
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 统计结果
     */
    PointStatistic getStatistic(Integer lineId, Date st, Date et);

    List<ScoreVO> getScore(Integer lineId, Date st, Date et);

    List<ScoreDailyVO> getScoreDaily(Integer lineId, Date st, Date et);

    Integer getCount(Integer lineId, Integer pointId, Date st, Date et);

    List<Integer> getCounts(Integer lineId, Date st, Date et);

    Integer getCountAvg(Integer lineId, Date st, Date et);

    Double getLineScore(Integer lineId, Date st, Date et);

    List<CountIdDTO> getLineScoreList(Integer lineId, Date st, Date et);

    List<String> getTopExceed(Integer lineId, Date st, Date et);


    List<CountStatisticDTO> getTopPoint(Integer lineId, Date st, Date et);

    List<CountStatisticDTO> getTopEquipment(Integer lineId, Date st, Date et);

    List<String> getRelationEquipment(Integer lineId, Date st, Date et);

    List<String> getRelationPoint(Integer lineId, Date st, Date et);

    Double getPlantScore(Integer plantId, Date st, Date et);


    List<PointExceedDTO> getPointExceedHistory(Integer lineId, Date st, Date et);

}




