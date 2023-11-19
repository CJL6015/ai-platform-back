package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 15:30
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DetectionTrendVO {
    private List<String> names;

    private List<Integer> counts;

    private List<List<Object[]>> values;

    private List<Integer> peopleMax;
    private List<Integer> countMax;
    private List<String> trend;

}
