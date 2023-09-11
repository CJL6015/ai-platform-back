package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 预警规则配置
 * @TableName warn_cfg
 */
@TableName(value ="warn_cfg")
@Data
public class WarnCfg implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 制药工序人员上限
     */
    private Integer pharmaceuticalProcessLimit;

    /**
     * 包装工序人员上限
     */
    private Integer packagingProcessLimit;

    /**
     * 装药工序人员上限
     */
    private Integer fillingProcessLimit;

    /**
     * 装车工序人员上限
     */
    private Integer loadingProcessLimit;

    /**
     * 生产线id
     */
    private Integer lineId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}