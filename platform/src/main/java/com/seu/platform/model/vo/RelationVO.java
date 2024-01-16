package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 19:51
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RelationVO {
    List<RelationTableVO> table;

    List<List<String>> groupData;
}
