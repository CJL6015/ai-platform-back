package com.seu.platform.model.dto;

import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-23 22:33
 */
@Data
public class PointExceedDTO {
    private String name;

    private Integer count;

    private Integer highCount;

    private Double time;

    private Double highTime;

    public Integer getExceed() {
        if (count == null && highCount == null) {
            return null;
        }
        count = count == null ? 0 : count;
        highCount = highCount == null ? 0 : highCount;
        return count + highCount;
    }

    public Double getScore(Double score, Double highScore, int day) {
        day = Math.max(1, day);
        count = count == null ? 0 : count;
        highCount = highCount == null ? 0 : highCount;
        return (count * score + highCount * highScore) / day;
    }
}
