package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 20:29
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PointValueVO {
    /**
     * 时间戳
     */
    private List<Long> timestamps;

    /**
     * 值
     */
    private List<Float> values;
}
