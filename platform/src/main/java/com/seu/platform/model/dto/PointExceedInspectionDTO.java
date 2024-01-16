package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-13 21:30
 */
@Data
public class PointExceedInspectionDTO {
    private String name;

    private Integer upUpCount;

    private Integer lowLowCount;

    private Integer upCount;

    private Integer lowCount;
}
