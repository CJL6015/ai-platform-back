package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-23 20:02
 */
@Data
public class InspectionStatisticDTO {
    private String name;

    private Integer exceed;

    private Integer count;
}
