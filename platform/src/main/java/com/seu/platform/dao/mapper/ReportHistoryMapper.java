package com.seu.platform.dao.mapper;

import com.seu.platform.dao.entity.ReportHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;

/**
* @author 陈小黑
* @description 针对表【report_history】的数据库操作Mapper
* @createDate 2024-01-20 21:58:51
* @Entity com.seu.platform.dao.entity.ReportHistory
*/
public interface ReportHistoryMapper extends BaseMapper<ReportHistory> {
    Date getLastTime(Integer type);
}




