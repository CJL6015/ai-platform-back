package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 21:11
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkTrendVO {
    private List<Object[]> trend;

    private int[] hours;
}
