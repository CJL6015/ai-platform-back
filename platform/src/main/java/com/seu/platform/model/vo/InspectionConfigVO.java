package com.seu.platform.model.vo;

import lombok.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-11 22:58
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InspectionConfigVO {
    /**
     * 生产线id
     */
    private Integer lineId;
    /**
     * 巡检抓拍周期
     */
    private Integer inspectionCaptureInterval;

    /**
     * 巡检抓拍模式
     */
    private Integer inspectionCaptureMode;

    /**
     * 历史照片存储时间
     */
    private Integer historicalPhotoRetentionPeriod;
}
