package com.seu.platform.model.vo;

import lombok.*;

import java.util.Date;

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

    /**
     * 系统冻结起始时间
     */
    private Date systemFreezeSt;

    /**
     * 系统冻结结束时间
     */
    private Date systemFreezeEt;
}
