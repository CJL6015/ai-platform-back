package com.seu.platform.model.vo;

import lombok.*;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-28 15:20
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResultVO {
    /**
     * 检测图片地址
     */
    private String detectionPicturePath;

    /**
     * 检测时间
     */
    private Date time;

    /**
     * 人员数
     */
    private Integer peopleCount;
}
