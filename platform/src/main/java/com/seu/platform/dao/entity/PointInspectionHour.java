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
 * @TableName point_inspection_hour
 */
@TableName(value ="point_inspection_hour")
@Data
public class PointInspectionHour implements Serializable {
    /**
     * 
     */
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
    private Integer refreshAbnormal;

    /**
     * 
     */
    private Integer upLimitAlarm;

    /**
     * 
     */
    private Integer lowLimitAlarm;

    /**
     * 
     */
    private Integer upUpLimitAlarm;

    /**
     * 
     */
    private Integer lowLowLimitAlarm;

    /**
     * 
     */
    private Integer totalAlarm;

    /**
     * 
     */
    private Integer lineRunCondition;

    /**
     * 
     */
    private Integer equipmentRunCondition;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}