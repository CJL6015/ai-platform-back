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
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

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
    private Long stopTime;

    /**
     * 
     */
    private Long runTime;

    /**
     * 
     */
    private Integer normalRefresh;

    /**
     * 
     */
    private Integer exceptionRefresh;

    /**
     * 
     */
    private Integer thresholdUpLowExceeded;

    /**
     * 
     */
    private Integer thresholdUpupLowlowExceeded;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}