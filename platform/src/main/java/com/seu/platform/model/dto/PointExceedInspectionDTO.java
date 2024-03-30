package com.seu.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-13 21:30
 */
@Data
@Slf4j
@ToString
public class PointExceedInspectionDTO {
    private Integer lineId;

    private String name;

    private volatile Integer upUpCount;

    private volatile Integer lowLowCount;

    private volatile Integer upCount;

    private volatile Integer lowCount;

    private volatile Integer count;

    private volatile Boolean init;

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

    public String getScore(Double score, Double highScore, int day) {
        initData();
        score = score == null ? 0.1 : score;
        highScore = highScore == null ? 0.2 : highScore;
        double s = (getExceedCount() * score + getHighCount() * highScore) / day;
        return NumberUtil.decimalFormat("#.##", s);
    }


    public Integer getExceedCount() {
        return upCount + lowCount;
    }

    public Integer getHighCount() {
        return upUpCount + lowLowCount;
    }

    public void initData() {
        if (!Boolean.TRUE.equals(init)) {
            init = true;
            upUpCount = upUpCount == null ? 0 : upUpCount;
            lowLowCount = lowLowCount == null ? 0 : lowLowCount;
            upCount = upCount == null ? 0 : upCount;
            lowCount = lowCount == null ? 0 : lowCount;
            upCount = upCount - upUpCount;
            lowCount = lowCount - lowLowCount;
            log.info("当前巡检超限:{}", this);
        }
    }
}
