package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-11 22:32
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StatisticVO {
    /**
     * 超限点数
     */
    private Double thresholdExceeded;

    /**
     * 未超限点数
     */
    private Double thresholdWithin;

    /**
     * 停机时间
     */
    private Long stopTime;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 刷新正常
     */
    private Double normalRefresh;

    /**
     * 刷新异常
     */
    private Double exceptionRefresh;
}
