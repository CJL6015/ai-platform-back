package com.seu.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineSafeScoreDTO {
    private String lineName;
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

    private Double score;

    private Double last;

    private Double lastYear;

    private Integer period;

    public static LineSafeScoreDTO avg(LineSafeScoreDTO dto1, LineSafeScoreDTO dto2) {
        return LineSafeScoreDTO.builder()
                .runDay((dto1.getRunDay() + dto2.getRunDay()) / 2)
                .runHour((dto1.getRunHour() + dto2.getRunHour()) / 2.0)
                .peopleInspectionScore(getAvg(dto1.getPeopleInspectionScore(), dto2.getPeopleInspectionScore()))
                .peopleInspectionRate(getPercentAvg(dto1.getPeopleInspectionRate(), dto2.getPeopleInspectionRate()))
                .pointInspectionRate(getPercentAvg(dto1.getPeopleInspectionScore(), dto2.getPeopleInspectionScore()))
                .pointInspectionScore(getAvg(dto1.getPointInspectionScore(), dto2.getPointInspectionScore()))
                .peopleScore(getAvg(dto1.getPeopleScore(), dto2.getPeopleScore()))
                .topProcess("---")
                .topPoint("---")
                .pointScore(getAvg(dto1.getPointScore(), dto2.getPointScore()))
                .score((dto1.getScore() + dto2.getScore()) / 2)
                .last((dto1.getLast() + dto2.getLast()) / 2)
                .lastYear((dto1.getLastYear() + dto2.getLastYear()) / 2)
                .lineName("生产点")
                .build();
    }

    private static String getPercentAvg(String rate1, String rate2) {
        double percent1 = Double.parseDouble(rate1.replace("%", ""));
        double percent2 = Double.parseDouble(rate2.replace("%", ""));

        // Step 2: Calculate average
        double average = (percent1 + percent2) / 200;
        return NumberUtil.formatPercent(average, 2);
    }

    private static String getAvg(String num1, String num2) {
        double percent1 = Double.parseDouble(num1);
        double percent2 = Double.parseDouble(num2);
        double average = (percent1 + percent2) / 2;
        return NumberUtil.decimalFormat("#.##", average);
    }

}
