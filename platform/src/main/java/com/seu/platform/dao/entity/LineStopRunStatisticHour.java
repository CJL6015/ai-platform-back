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
 * @TableName line_stop_run_statistic_hour
 */
@TableName(value ="line_stop_run_statistic_hour")
@Data
public class LineStopRunStatisticHour implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 
     */
    private Integer lineId;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date startTime;

    /**
     * 
     */
    private Date endTime;

    /**
     * 
     */
    private Double stopHour;

    /**
     * 
     */
    private Double runHour;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}