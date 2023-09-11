package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 点号统计
 * @author 陈小黑
 * @TableName point_statistic
 */
@TableName(value ="point_statistic")
@Data
public class PointStatistic implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 超限点数
     */
    private Integer thresholdExceeded;

    /**
     * 未超限点数
     */
    private Integer thresholdWithin;

    /**
     * 停机时间
     */
    private Long stopTime;

    /**
     * 运行时间
     */
    private Long runTime;

    /**
     * 刷新正常
     */
    private Integer normalRefresh;

    /**
     * 刷新异常
     */
    private Integer exceptionRefresh;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 生产线id
     */
    private Integer lineId;

    /**
     * 测点类型
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}