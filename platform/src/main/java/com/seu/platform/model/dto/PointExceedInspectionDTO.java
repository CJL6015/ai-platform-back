package com.seu.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-13 21:30
 */
@Data
public class PointExceedInspectionDTO {
    private String name;

    private Integer upUpCount;

    private Integer lowLowCount;

    private Integer upCount;

    private Integer lowCount;

    private Integer count;

    public Integer getExceed() {
        initData();
        return upUpCount + lowLowCount + lowCount + upCount;
    }

    public String getRate() {
        if (count == null || count == 0) {
            return "0%";
        }
        initData();
        Integer exceed = getExceed();
        return NumberUtil.formatPercent(1.0 * exceed / count, 2);
    }

    public String getScore(Double score, Double highScore) {
        initData();
        score = score == null ? 0.1 : score;
        highScore = highScore == null ? 0.2 : highScore;
        return NumberUtil.decimalFormat("#.##", getCount() * score + getHighCount() * highScore);
    }


    public Integer getExceedCount() {
        return upCount + lowCount;
    }

    public Integer getHighCount() {
        return upUpCount + lowLowCount;
    }

    public void initData() {
        upUpCount = upUpCount == null ? 0 : upUpCount;
        lowLowCount = lowLowCount == null ? 0 : lowLowCount;
        upCount = upCount == null ? 0 : upCount;
        lowCount = lowCount == null ? 0 : lowCount;
    }
}
