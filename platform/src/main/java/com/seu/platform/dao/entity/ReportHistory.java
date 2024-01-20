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
 * @TableName report_history
 */
@TableName(value ="report_history")
@Data
public class ReportHistory implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 创建时间
     */
    private Date time;

    /**
     * 报表类型
     */
    private Integer type;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}