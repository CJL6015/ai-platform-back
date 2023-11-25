package com.seu.platform.model.param;

import lombok.Data;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-25 15:13
 */
@Data
public class BenchmarkQuery {
    private List<Integer> line1;
    private List<Integer> line2;
    private List<Integer> line3;
    private Integer time;
    private List<Integer> totalIndicators;
    private List<Integer> subIndicators;
    private Integer extremeNumber;
    private List<Integer> extremeIndicators;
    private List<Integer> trend;
    private Integer correlationNumber;
    private List<Integer> correlation;
}
