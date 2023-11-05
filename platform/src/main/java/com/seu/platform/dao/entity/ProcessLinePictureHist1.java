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
 * @TableName process_line_picture_hist_1
 */
@TableName(value ="process_line_picture_hist_1")
@Data
public class ProcessLinePictureHist1 implements Serializable {
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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}