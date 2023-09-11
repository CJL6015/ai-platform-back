package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointStatistic;
import com.seu.platform.model.vo.StatisticVO;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic(点号统计)】的数据库操作Service
 * @createDate 2023-09-11 22:33:34
 */
public interface PointStatisticService extends IService<PointStatistic> {
    /**
     * 获得统计结果
     *
     * @param lineId 生产线id
     * @return vo
     */
    StatisticVO getOneVo(Integer lineId);
}
