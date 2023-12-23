package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName user_cfg
 */
@TableName(value ="user_cfg")
@Data
public class UserCfg implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实名称
     */
    private String realName;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 
     */
    private String description;

    /**
     * token
     */
    private String token;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 主页面地址
     */
    private String homePath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}