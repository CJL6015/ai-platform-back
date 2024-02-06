package com.seu.platform.model.dto;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-20 14:09
 */
@Data
public class ExceedDTO {
    private Integer lineId;

    private Integer total;

    private Integer exceed;

    public String getRate() {
        if (total == null || total == 0) {
            return "0%";
        }
        exceed = exceed == null ? 0 : exceed;
        return NumberUtil.formatPercent(1.0 * exceed / total, 2);
    }

    public String getScore(Double score) {
        exceed = exceed == null ? 0 : exceed;
        return NumberUtil.decimalFormat("#.##", exceed * score);
    }
}
