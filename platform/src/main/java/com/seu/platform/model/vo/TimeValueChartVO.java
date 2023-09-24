package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author 陈小黑
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimeValueChartVO {
    private List<String> timestamps;

    private List<Integer> values;
}
