package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:47
 */
@Data
@ToString
public class TrendDTO {
    private Date time;
    private Integer count;
}
