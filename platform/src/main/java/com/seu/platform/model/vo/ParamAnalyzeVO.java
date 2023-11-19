package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:11
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class ParamAnalyzeVO {
    private Integer month;
    private Integer quarter;
    private Integer year;
    private Integer lastMonth;
    private Integer lastQuarter;
    private Integer lastYearMonth;
    private Integer lastYearQuarter;
    private Integer lastYear;
}
