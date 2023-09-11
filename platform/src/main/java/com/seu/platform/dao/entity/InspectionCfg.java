package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 巡检抓拍规则
 * @author 陈小黑
 * @TableName inspection_cfg
 */
@TableName(value ="inspection_cfg")
@Data
public class InspectionCfg implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 巡检抓拍周期
     */
    private Integer inspectionCaptureInterval;

    /**
     * 巡检抓拍模式
     */
    private Integer inspectionCaptureMode;

    /**
     * 历史照片存储时间
     */
    private Integer historicalPhotoRetentionPeriod;

    /**
     * 系统冻结起始时间
     */
    private Date systemFreezeSt;

    /**
     * 系统冻结结束时间
     */
    private Date systemFreezeEt;

    /**
     * 生产线id
     */
    private Integer lineId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}