package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName equipment_remaining_life_cfg
 */
@TableName(value = "equipment_remaining_life_cfg")
@Data
public class EquipmentRemainingLifeCfg implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 设备id
     */
    private Integer relatedEquipmentId;
    /**
     * 生产线id
     */
    private Integer lineId;
    /**
     * 描述
     */
    private String description;
    /**
     * 开始时间
     */
    private Date startUseTime;
    /**
     * 设计使用寿命
     */
    private Integer designedUsingDays;
    /**
     * 危险程度
     */
    private String dangerDegree;
}