package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:37
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrendVO<K, V> {
    /**
     * 时间
     */
    private List<K> times;

    /**
     * 值
     */
    private List<V> value;

    /**
     * 拟合值
     */
    private List<Double> fitValue;

    /**
     * 斜率参数
     */
    private double[] params;
}
