package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.TimeRange;

import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic_hour】的数据库操作Service
 * @createDate 2023-09-23 16:14:08
 */
public interface PointStatisticHourService extends IService<PointStatisticHour> {


    /**
     * 查询测点统计结果
     *
     * @param lineId    生产线id
     * @param timeRange 时间
     * @return 数据
     */
    List<PointStatisticVO> getPointStatistic(Integer lineId, TimeRange timeRange);
}
