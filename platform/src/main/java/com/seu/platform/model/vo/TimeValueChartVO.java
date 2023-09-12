package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TimeValueChartVO {
    private List<Long> timestamps;

    private List<Integer> values;
}
