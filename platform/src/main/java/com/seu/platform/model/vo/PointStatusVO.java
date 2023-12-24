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
public class PointStatusVO {
    /**
     * 点号名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 超限时间
     */
    private Long exceededTime;

    /**
     * 刷新异常时间
     */
    private Long exceptionTime;

    /**
     * 运行状态
     */
    private Boolean status;

    /**
     * 是否超限
     */
    private Boolean isExceeded;
}
