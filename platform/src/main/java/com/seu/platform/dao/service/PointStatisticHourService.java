package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.BenchmarkDataVO;
import com.seu.platform.model.vo.TrendDetailVO;
import com.seu.platform.model.vo.TrendVO;

import java.util.Date;

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
     * @return 数据
     */
    BenchmarkDataVO getBenchmarkData(Integer lineId);

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

}
