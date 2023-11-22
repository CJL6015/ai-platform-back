package com.seu.platform.service;

import com.seu.platform.model.vo.AnalyzeVO;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:09
 */
public interface AnalyzeService {
    /**
     * 获取分析数据
     *
     * @param pointId 测点id
     * @return 数据
     */
    AnalyzeVO getParamAnalyze(Integer pointId);

    /**
     * 获取分析数据
     *
     * @param cameraIp 工序
     * @return 数据
     */
    AnalyzeVO getPeopleAnalyze(String cameraIp);
}
