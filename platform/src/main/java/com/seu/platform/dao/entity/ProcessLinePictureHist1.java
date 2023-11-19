package com.seu.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName process_line_picture_hist_1
 */
@TableName(value = "process_line_picture_hist_1")
@Data
public class ProcessLinePictureHist1 implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     *
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
     *
     */
    private String picturePath;
    /**
     *
     */
    private Integer peopleHasChecked;
    /**
     *
     */
    private String detectionPicturePath;
    /**
     *
     */
    private Integer peopleCount;
    /**
     * 是否冻结
     */
    private Integer freeze;
}