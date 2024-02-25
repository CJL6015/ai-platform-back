package com.seu.platform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-25 19:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountStatisticDTO {
    private String name;

    private Integer count;
}
