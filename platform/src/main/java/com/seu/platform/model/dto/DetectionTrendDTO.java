package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 15:14
 */
@Data
@ToString
public class DetectionTrendDTO {
    private Integer id;

    private String name;

    private Integer count;

    private Integer maxCount;

    private Date time;
}
