package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-10 15:08
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RelationTableVO {
    private String sub;

    private String target;

    private String conf;

    private String support;

    private String relevant;

    private String remark;
}
