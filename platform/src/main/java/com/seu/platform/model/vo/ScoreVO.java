package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 18:30
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoreVO {
    private Integer id;

    private String name;

    private Double score;
}
