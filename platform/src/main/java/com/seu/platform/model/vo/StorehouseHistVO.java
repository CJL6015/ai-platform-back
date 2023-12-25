package com.seu.platform.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-24 21:39
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorehouseHistVO {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date time;

    private Integer limit;

    private Double stock;

    private String exceed;
}
