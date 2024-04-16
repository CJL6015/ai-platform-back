package com.seu.platform.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

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
    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
    private Date time;

    private String degree;

    private Double design;

    private Double up;

    private Double low;

    private String remain;

    private String expire;

}
