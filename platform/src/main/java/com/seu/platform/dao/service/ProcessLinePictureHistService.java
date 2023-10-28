package com.seu.platform.dao.service;

import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【process_line_picture_hist】的数据库操作Service
* @createDate 2023-10-28 10:48:15
*/
public interface ProcessLinePictureHistService extends IService<ProcessLinePictureHist> {
    /**
     * 获取等待检测的图片
     *
     * @param count 个数
     * @return 等待检测的图片
     */
    List<ProcessLinePictureHist> getPendingChecks(int count);
}
