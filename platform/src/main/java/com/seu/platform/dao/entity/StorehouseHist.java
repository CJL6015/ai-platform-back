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
 * @TableName storehouse_hist
 */
@TableName(value ="storehouse_hist")
@Data
public class StorehouseHist implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Integer storehouseId;

    /**
     * 
     */
    private Double storeCount;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private Date storeRecordTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}