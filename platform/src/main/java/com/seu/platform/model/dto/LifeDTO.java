package com.seu.platform.model.dto;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-04-04 21:18
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class LifeDTO {
    private Integer id;

    private String name;

    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private DateTime time;

    private DateTime degree;

    private Integer design;

    private Float remain;
}
