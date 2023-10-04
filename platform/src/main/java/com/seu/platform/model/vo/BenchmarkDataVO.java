package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 14:39
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BenchmarkDataVO {
    /**
     * 设备
     */
    private List<String> equipments;
    /**
     * 季度对标数据
     */
    private List<List<Integer>> quarterData;
    /**
     * 月对标数据
     */
    private List<List<Integer>> monthData;
    /**
     * 日对标数据
     */
    private List<List<Integer>> dayData;
}
