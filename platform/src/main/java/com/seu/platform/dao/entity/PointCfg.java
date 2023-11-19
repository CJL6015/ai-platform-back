package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 测点基表
 *
 * @TableName point_cfg
 */
@TableName(value = "point_cfg")
@Data
public class PointCfg implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
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
    /**
     * 测点类型
     */
    private Integer pointType;
    /**
     * 高高限
     */
    private Double upperUpperLimit;
    /**
     * 下下限
     */
    private Double lowerLowerLimit;
    /**
     * 设备id
     */
    private Integer equipmentId;
    /**
     * 刷新状态
     */
    private Integer state;
    /**
     * 持续时间阈值
     */
    private Integer threshold;
    /**
     * 该测点历史平均超限时间
     */
    private Integer duration;
    /**
     * 超限扣分
     */
    private Double score;
    /**
     * 超高高限扣分
     */
    private Double highScore;
}