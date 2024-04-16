package com.seu.platform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-04-06 10:16
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class LifeTrendDTO {
    private Date time;

    private Double up;

    private Double low;
}
