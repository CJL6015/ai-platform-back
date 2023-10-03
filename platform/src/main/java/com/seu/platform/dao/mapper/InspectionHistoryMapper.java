package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.InspectionHistory;

import java.util.Date;

/**
 * @author 陈小黑
 * @description 针对表【inspection_history(巡检历史)】的数据库操作Mapper
 * @createDate 2023-09-11 22:17:07
 * @Entity com.seu.platform.dao.entity.InspectionHistory
 */
public interface InspectionHistoryMapper extends BaseMapper<InspectionHistory> {
    /**
     * 获取总超员次数
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 超员次数
     */
    Integer getAllExceededNum(Integer lineId, Date st, Date et);
}




