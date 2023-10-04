package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 14:43
 */
@Data
@ToString
public class BenchmarkDTO {
    private Integer equipmentId;

    private String equipmentName;

    private Integer count;

    private Date time;

}
