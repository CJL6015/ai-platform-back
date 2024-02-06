package com.seu.platform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineSafeScoreDTO {
    private Integer runDay;

    private Double runHour;

    private String peopleInspectionRate;

    private String peopleInspectionScore;

    private String pointInspectionRate;

    private String pointInspectionScore;

    private String peopleScore;

    private String topProcess;

    private String pointScore;

    private String topPoint;

    private String score;

    private String last;

    private String lastYear;

}
