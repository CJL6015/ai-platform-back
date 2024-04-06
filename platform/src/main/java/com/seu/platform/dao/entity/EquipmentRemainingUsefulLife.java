package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName equipment_remaining_useful_life
 */
@TableName(value = "equipment_remaining_useful_life")
@Data
public class EquipmentRemainingUsefulLife implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId
    private Integer id;
    /**
     * 配置id
     */
    private Integer equipmentUsefulLifeId;
    /**
     * 生产线id
     */
    private Integer lineId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 剩余使用寿命上限
     */
    private Double remainingUsefulDaysUp;
    /**
     * 剩余使用寿命下限
     */
    private Double remainingUsefulDaysLow;
}