package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 测点基表
 * @TableName point_cfg
 */
@TableName(value ="point_cfg")
@Data
public class PointCfg implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 点号名称
     */
    private String name;

    /**
     * 点号id
     */
    private Integer pointId;

    /**
     * 描述
     */
    private String description;

    /**
     * 单位
     */
    private String unit;

    /**
     * 超限时间
     */
    private Long exceededTime;

    /**
     * 刷新异常时间
     */
    private Long exceptionTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 生产线id
     */
    private Integer lineId;

    /**
     * 上限
     */
    private Double upperLimit;

    /**
     * 
     */
    private Double lowerLimit;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}