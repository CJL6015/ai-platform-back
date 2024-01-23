package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-23 22:33
 */
@Data
public class PointExceedDTO {
    private String name;

    private Integer count;

    private Integer highCount;

    private Double time;

    private Double highTime;
}
