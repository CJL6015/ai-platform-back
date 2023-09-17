package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 冻结日志
 * @TableName freeze_log
 */
@TableName(value ="freeze_log")
@Data
public class FreezeLog implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 生产线id
     */
    private Integer lineId;

    /**
     * 状态
     */
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}