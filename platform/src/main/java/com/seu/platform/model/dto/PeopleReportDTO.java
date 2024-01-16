package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 20:30
 */
@Data
@ToString
public class PeopleReportDTO {
    private Integer count;

    private Integer exceed;

    private String ip;
}
