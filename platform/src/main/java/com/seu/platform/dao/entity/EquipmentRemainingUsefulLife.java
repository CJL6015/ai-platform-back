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
 * @TableName equipment_remaining_useful_life
 */
@TableName(value ="equipment_remaining_useful_life")
@Data
public class EquipmentRemainingUsefulLife implements Serializable {
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
     * 剩余使用sho
     */
    private Double remainingUsefulDays;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}