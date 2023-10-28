package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-11 22:43
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointStatisticVO {
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
     * 时实值
     */
    private Float value;

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

    /**
     * 是否超限
     */
    private Boolean isExceeded;
}
