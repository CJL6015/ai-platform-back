package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-12 22:17
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class BenchmarkChartVO {
    private List<String> names;

    private List<List<Integer>> values;

    private Double rate;

    private Integer max;
}
