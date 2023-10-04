package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 21:24
 */
@Data
@ToString
public class HourTrendDTO {
    private Integer time;
    private Integer count;
}
