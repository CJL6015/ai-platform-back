package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName point_statistic_hour
 */
@TableName(value ="point_statistic_hour")
@Data
public class PointStatisticHour implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Integer pointId;

    /**
     * 
     */
    private Integer lineId;

    /**
     * 
     */
    private Integer equipmentId;

    /**
     * 
     */
    private Double stopTime;

    /**
     * 
     */
    private Double runTime;

    /**
     * 
     */
    private Double normalRefresh;

    /**
     * 
     */
    private Double exceptionRefresh;

    /**
     * 
     */
    private Double thresholdUpLowExceeded;

    /**
     * 
     */
    private Double thresholdUpupLowlowExceeded;

    /**
     * 
     */
    private String upExceededDetails;

    /**
     * 
     */
    private String lowExceededDetails;

    /**
     * 
     */
    private String upupExceededDetails;

    /**
     * 
     */
    private String lowlowExceededDetails;

    /**
     * 
     */
    private String exceptionFreshDetails;

    /**
     * 
     */
    private Integer upLowExceededCount;

    /**
     * 
     */
    private Integer upupLowlowExceededCount;

    /**
     * 
     */
    private Date statiaticStartTime;

    /**
     * 
     */
    private Date statisticEndTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}