package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-04-06 19:16
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RelationCacheVO {
    public Integer lineId;

    public Double limit;

    public RelationVO relation;
}
