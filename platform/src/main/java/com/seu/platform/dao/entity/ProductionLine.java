package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 生产线
 * @TableName production_line
 */
@TableName(value ="production_line")
@Data
public class ProductionLine implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 生产线名称
     */
    private String name;

    /**
     * 公司id
     */
    private Integer plantId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}