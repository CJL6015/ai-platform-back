package com.seu.platform.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-03 12:10
 */
@Data
@ToString
public class InspectionHistoryDTO {
    private Integer id;
    /**
     * 起始时间
     */
    private Date st;

    /**
     * 结束时间
     */
    private Date et;

    /**
     * 冻结时长(分)
     */
    private Float freezeTime;

    /**
     * 超员次数
     */
    private Integer exceededNum;

    /**
     * 超位次数
     */
    private Integer exceededPeople;

    /**
     * 图片地址
     */
    private String imageUrl;


    /**
     * 生产线id
     */
    private Integer lineId;
}
