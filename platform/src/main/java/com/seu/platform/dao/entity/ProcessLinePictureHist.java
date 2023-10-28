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
 * @TableName process_line_picture_hist
 */
@TableName(value ="process_line_picture_hist")
@Data
public class ProcessLinePictureHist implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String cameraId;

    /**
     * 
     */
    private Date updateTime;

    /**
     * 原始图片地址
     */
    private String picturePath;

    /**
     * 是否检测:0-未检测;1-已检测
     */
    private Integer peopleHasChecked;

    /**
     * 检测后地址
     */
    private String detectionPicturePath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}