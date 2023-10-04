package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName equipment_cfg
 */
@TableName(value ="equipment_cfg")
@Data
public class EquipmentCfg implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 
     */
    private Integer lineId;

    /**
     * 
     */
    private String equipmentDescription;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}