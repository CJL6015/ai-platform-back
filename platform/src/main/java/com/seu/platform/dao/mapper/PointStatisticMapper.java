package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.PointStatistic;

import java.util.Date;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic(点号统计)】的数据库操作Mapper
 * @createDate 2023-09-11 22:33:34
 * @Entity com.seu.platform.dao.entity.PointStatistic
 */
public interface PointStatisticMapper extends BaseMapper<PointStatistic> {
    /**
     * 获取统计结果
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 统计结果
     */
    PointStatistic getStatistic(Integer lineId, Date st, Date et);

}




