package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:33
 */
@Data
@ToString
public class EquipmentTrendDTO {
    private Integer id;
    private String equipmentName;
    private Integer count;
    private Date time;
}
