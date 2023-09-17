package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.InspectionHistory;
import com.seu.platform.model.vo.InspectionHistoryDataVO;

import java.util.Date;

/**
 * @author 陈小黑
 * @description 针对表【inspection_history(巡检历史)】的数据库操作Service
 * @createDate 2023-09-11 22:17:07
 */
public interface InspectionHistoryService extends IService<InspectionHistory> {
    /**
     * 获取历史数据
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 历史数据
     */
    InspectionHistoryDataVO getInspectionHistoryValue(Integer lineId, Date st, Date et);
}
