package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.model.dto.PointStatisticDTO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_cfg(测点基表)】的数据库操作Mapper
 * @createDate 2023-09-11 22:24:42
 * @Entity com.seu.platform.dao.entity.PointCfg
 */
public interface PointCfgMapper extends BaseMapper<PointCfg> {
    /**
     * 获取统计结果
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 统计结果
     */
    List<PointStatisticDTO> getPointStatistic(Integer lineId, Date st, Date et);
}




