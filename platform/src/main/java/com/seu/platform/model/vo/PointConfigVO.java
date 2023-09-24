package com.seu.platform.model.vo;

import lombok.*;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-24 21:52
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointConfigVO {
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
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 生产线id
     */
    private Integer lineId;

    /**
     * 上限
     */
    private Double upperLimit;

    /**
     *
     */
    private Double lowerLimit;

    /**
     * 测点类型
     */
    private Integer pointType;

    /**
     * 高高限
     */
    private Double upperUpperLimit;

    /**
     * 下下限
     */
    private Double lowerLowerLimit;

    /**
     * 设备id
     */
    private Integer equipmentId;

    /**
     * 刷新状态
     */
    private Integer state;

    /**
     * 持续时间阈值
     */
    private Integer threshold;

    /**
     * 该测点历史平均超限时间
     */
    private Integer duration;
}
