package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 16:22
 */
@Data
@ToString
public class PointStatisticDTO {
    /**
     * 点号名称
     */
    private String name;

    /**
     * 点号id
     */
    private Integer pointId;

    /**
     * 描述
     */
    private String description;

    /**
     * 单位
     */
    private String unit;

    /**
     * 超限时间
     */
    private Long exceededTime;

    /**
     * 刷新异常时间
     */
    private Long exceptionTime;

    /**
     * 刷新状态
     */
    private Integer state;

    /**
     * 上限
     */
    private Float upperLimit;

    /**
     * 下限
     */
    private Float lowerLimit;
}
