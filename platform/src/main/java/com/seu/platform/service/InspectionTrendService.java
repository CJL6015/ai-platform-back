package com.seu.platform.service;

import com.seu.platform.model.vo.TrendVO;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:41
 */
public interface InspectionTrendService {

    /**
     * 查询超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 超限趋势
     */
    TrendVO<String, Integer> getPointInspectionTrendMonth(Integer lineId, Date st, Date et);
}
