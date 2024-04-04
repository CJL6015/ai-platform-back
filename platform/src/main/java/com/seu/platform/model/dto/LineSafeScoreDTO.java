package com.seu.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private List<CountStatisticDTO> topProcessList;

    private List<CountStatisticDTO> topPointList;


    public static LineSafeScoreDTO avg(LineSafeScoreDTO dto1, LineSafeScoreDTO dto2) {
        if (dto1.getRunDay() == 0) {
            return dto2;
        }
        if (dto2.getRunDay() == 0) {
            return dto1;
        }
        Double last = null, lastYear = null;
        if (dto1.getLast() != null && dto2.getLast() != null) {
            last = (dto1.getLast() + dto2.getLast()) / 2;
        } else if (dto1.getLast() != null) {
            last = dto1.getLast();
        } else if (dto2.getLast() != null) {
            last = dto2.getLast();
        }
        if (dto1.getLastYear() != null && dto2.getLastYear() != null) {
            lastYear = (dto1.getLastYear() + dto2.getLastYear()) / 2;
        } else if (dto1.getLastYear() != null) {
            lastYear = dto1.getLastYear();
        } else if (dto2.getLastYear() != null) {
            lastYear = dto2.getLastYear();
        }
        double score = 0;
        int count = 0;
        Double score1 = dto1.getScore();
        if (score1 != null) {
            score += score1;
            count++;
        }
        Double score2 = dto2.getScore();
        if (score2 != null) {
            score += score2;
            count++;
        }
        Double s = count == 0 ? null : score / count;
        return LineSafeScoreDTO.builder()
                .runDay((dto1.getRunDay() + dto2.getRunDay()) / 2)
                .runHour((dto1.getRunHour() + dto2.getRunHour()) / 2.0)
                .peopleInspectionScore(getAvg(dto1.getPeopleInspectionScore(), dto2.getPeopleInspectionScore()))
                .peopleInspectionRate(getPercentAvg(dto1.getPeopleInspectionRate(), dto2.getPeopleInspectionRate()))
                .pointInspectionRate(getPercentAvg(dto1.getPeopleInspectionScore(), dto2.getPeopleInspectionScore()))
                .pointInspectionScore(getAvg(dto1.getPointInspectionScore(), dto2.getPointInspectionScore()))
                .peopleScore(getAvg(dto1.getPeopleScore(), dto2.getPeopleScore()))
                .topProcess(getTopName(dto1.getTopProcessList(), dto2.getTopProcessList(), 1))
                .topPoint(getTopName(dto1.getTopPointList(), dto2.getTopPointList(), 3))
                .pointScore(getAvg(dto1.getPointScore(), dto2.getPointScore()))
                .score(s)
                .last(last)
                .lastYear(lastYear)
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

    private static String getTopName(List<CountStatisticDTO> list1, List<CountStatisticDTO> list2, int limit) {
        return Stream.concat(list1.stream(), list2.stream())
                .sorted(Comparator.comparingInt(CountStatisticDTO::getCount).reversed())
                .limit(limit)
                .map(CountStatisticDTO::getName)
                .collect(Collectors.joining(","));
    }

}
