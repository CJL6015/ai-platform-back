package com.seu.platform.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * @author 陈小黑
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InspectionHistoryVO {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date st;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date et;

    /**
     * 冻结时长(分)
     */
    private Integer freezeTime;

    /**
     * 超员次数
     */
    private Integer exceededNum;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 是否冻结:0-是;1-否
     */
    private Integer freeze;
}
