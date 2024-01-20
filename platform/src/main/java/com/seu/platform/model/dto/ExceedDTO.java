package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-20 14:09
 */
@Data
public class ExceedDTO {
    private Integer lineId;

    private Integer total;

    private Integer exceed;
}
