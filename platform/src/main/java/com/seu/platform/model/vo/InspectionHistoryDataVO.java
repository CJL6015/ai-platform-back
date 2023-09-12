package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InspectionHistoryDataVO {
    private TimeValueChartVO chartValue;

    private List<InspectionHistoryVO> tableValue;
}
