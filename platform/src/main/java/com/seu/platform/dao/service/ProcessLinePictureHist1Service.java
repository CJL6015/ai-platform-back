package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.entity.ProcessLinePictureHist1;
import com.seu.platform.model.vo.DetectionResultVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.model.vo.TrendVO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist_1】的数据库操作Service
 * @createDate 2023-10-30 23:54:52
 */
public interface ProcessLinePictureHist1Service extends IService<ProcessLinePictureHist1> {
    /**
     * 获取等待检测的图片
     *
     * @param count 个数
     * @return 等待检测的图片
     */
    List<ProcessLinePictureHist1> getPendingChecks(int count);


    /**
     * 获取检测结果
     *
     * @param ips  摄像机ip
     * @param time 时间
     * @return 检测结果
     */
    List<DetectionResultVO> getDetectionResult(List<String> ips, Date time);

    /**
     * 获取月趋势
     *
     * @param timeRange 时间范围
     * @return 趋势
     */
    TrendVO<String, Integer> getTrendMonth(TimeRange timeRange);

    /**
     * 获取日趋势
     *
     * @param timeRange 时间范围
     * @return 趋势
     */
    TrendVO<String, Integer> getTrendDaily(TimeRange timeRange);
}
