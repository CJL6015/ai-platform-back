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
     * 组名
     */
    private String groupName;
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
}
