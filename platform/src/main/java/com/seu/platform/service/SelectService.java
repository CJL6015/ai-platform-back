package com.seu.platform.service;

import com.seu.platform.model.vo.OptionItemVO;
import com.seu.platform.model.vo.SelectAllOptionVO;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-09 16:11
 */
public interface SelectService {
    /**
     * 获取所有的下拉框选项
     *
     * @return 下拉框选项
     */
    SelectAllOptionVO getAllOptions();

    /**
     * 获取生产线选项
     *
     * @param plantId 公司id
     * @return 生产线选项
     */
    List<OptionItemVO> getLinesOptions(Integer plantId);

    /**
     * 获取设备选项
     *
     * @param lineId 生产线id
     * @return 设备选项
     */
    List<OptionItemVO> getEquipments(Integer lineId);

    /**
     * 获取所有参数
     *
     * @return 所有参数
     */
    List<OptionItemVO> getPoints(Integer lineId);
}
