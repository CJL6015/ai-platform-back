package com.seu.platform.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:29
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentTrendVO {

    private List<String> equipments;

    private List<List<Object[]>> values;

    private List<Integer> counts;

    private List<Integer> equipmentMax;
}
