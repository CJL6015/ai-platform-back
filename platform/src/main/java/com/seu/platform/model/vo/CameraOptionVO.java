package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-03-30 14:12
 */
@Data
@Builder
@ToString
@AllArgsConstructor
public class CameraOptionVO {
    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
}
