package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-09 16:13
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class SelectAllOptionVO {
    /**
     * 公司选项
     */
    private List<OptionItemVO> plantOptions;

    /**
     * 生产线选项
     */
    private List<OptionItemVO> linesOptions;
}
