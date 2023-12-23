package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-23 18:55
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class StatusVO {
    private String name;

    private Boolean status;

    private Boolean warn;
}
