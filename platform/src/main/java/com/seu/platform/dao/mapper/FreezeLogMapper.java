package com.seu.platform.dao.mapper;

import com.seu.platform.dao.entity.FreezeLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.model.dto.InspectionHistoryDTO;

import java.util.Date;
import java.util.List;

/**
* @author 陈小黑
* @description 针对表【freeze_log(冻结日志)】的数据库操作Mapper
* @createDate 2023-09-16 16:16:52
* @Entity com.seu.platform.dao.entity.FreezeLog
*/
public interface FreezeLogMapper extends BaseMapper<FreezeLog> {
    /**
     * 获取冻结时期的历史
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 历史
     */
    List<InspectionHistoryDTO> getHistoryFreeze(Integer lineId, Date st, Date et);
}




