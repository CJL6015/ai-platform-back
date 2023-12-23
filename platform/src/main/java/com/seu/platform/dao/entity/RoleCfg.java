package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName role_cfg
 */
@TableName(value ="role_cfg")
@Data
public class RoleCfg implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色值
     */
    private String value;

    /**
     * 主页地址
     */
    private String homePath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}