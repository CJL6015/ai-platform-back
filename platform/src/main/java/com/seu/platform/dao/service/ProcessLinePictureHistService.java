package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.model.vo.DetectionResultVO;

import java.util.Date;
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


    /**
     * 获取检测结果
     *
     * @param ips 摄像机ip
     * @param time 时间
     * @return 检测结果
     */
    List<DetectionResultVO> getDetectionResult(List<String> ips, Date time);
}
