package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-16 22:39
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class OptionItemVO {
    /**
     * id
     */
    private Integer id;
    /**
     * 名称
     */
    private String name;
}
