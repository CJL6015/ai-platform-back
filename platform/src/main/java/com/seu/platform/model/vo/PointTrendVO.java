package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-25 21:52
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointTrendVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 时间
     */
    private List<String> times;

    /**
     * 值
     */
    private List<Float> value;

    /**
     * 上限
     */
    private Double upperLimit;

    /**
     * 下限
     */
    private Double lowerLimit;

    /**
     * 高高限
     */
    private Double upperUpperLimit;

    /**
     * 下下限
     */
    private Double lowerLowerLimit;
}
