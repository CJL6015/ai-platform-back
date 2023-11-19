package com.seu.platform.service;

import com.seu.platform.model.vo.BenchmarkTrendVO;
import com.seu.platform.model.vo.EquipmentTrendVO;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:31
 */
public interface BenchmarkService {
    /**
     * 查询设备超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    EquipmentTrendVO getEquipmentTrend(Integer lineId, Date st, Date et);


    /**
     * 查询总超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    BenchmarkTrendVO getBenchmarkTrend(Integer lineId, Date st, Date et);

}
