package com.seu.platform.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-25 16:27
 */
@Getter
@AllArgsConstructor
public enum LineEnum {
    /**
     * 生产线
     */
    EMULSIFICATION(1, "荆门乳化产线"),
    EXPANSION(2, "荆门膨化产线"),
    DETONATOR_A(3, "辽宁雷管二期A线"),
    DETONATOR_B(4, "辽宁雷管二期B线");

    private final Integer code;

    private final String desc;

    public static String getNameByCode(Integer code) {
        LineEnum[] values = LineEnum.values();
        for (LineEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return "";
    }

}
