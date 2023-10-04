package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 17:36
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrendDetailVO {
    private List<String> equipments;

    private List<List<Integer>> data;

    private List<String> times;
}
