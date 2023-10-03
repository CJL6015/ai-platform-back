package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.FreezeLog;
import com.seu.platform.model.vo.InspectionHistoryDataVO;

import java.util.Date;

/**
 * @author 陈小黑
 */
public interface FreezeLogService extends IService<FreezeLog> {
    /**
     * 查询冻结期间数据
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 巡检结果
     */
    InspectionHistoryDataVO getInspectionHistoryFreeze(Integer lineId, Date st, Date et);
}
