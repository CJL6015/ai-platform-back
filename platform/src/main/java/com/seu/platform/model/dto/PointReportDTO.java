package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-13 15:16
 */
@Data
public class PointReportDTO {
    private Integer id;
    /**
     * 超限次数
     */
    private Integer exceed;

    /**
     * 巡检次数
     */
    private Integer count;
}
