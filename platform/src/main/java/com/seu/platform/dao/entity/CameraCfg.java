package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName camera_cfg
 */
@TableName(value ="camera_cfg")
@Data
public class CameraCfg implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String cameraDescription;

    /**
     * 
     */
    private Integer processId;

    /**
     * 
     */
    private Integer lineId;

    /**
     * 
     */
    private String cameraIp;

    /**
     * 
     */
    private String cameraPort;

    /**
     * 
     */
    private String cameraAccount;

    /**
     * 
     */
    private String cameraPassword;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}