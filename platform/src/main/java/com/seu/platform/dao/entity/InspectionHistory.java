package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 巡检历史
 * @TableName inspection_history
 */
@TableName(value ="inspection_history")
@Data
public class InspectionHistory implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 起始时间
     */
    private Date st;

    /**
     * 结束时间
     */
    private Date et;

    /**
     * 冻结时长(分)
     */
    private Integer freezeTime;

    /**
     * 超员次数
     */
    private Integer exceededNum;

    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 是否冻结:0-是;1-否
     */
    private Integer freeze;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}