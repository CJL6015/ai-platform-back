package com.seu.platform.model.vo;

import lombok.*;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 19:26
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ScoreDailyVO {
    private Double score;

    private Date time;
}
