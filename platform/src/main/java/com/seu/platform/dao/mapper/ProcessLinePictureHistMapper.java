package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.vo.DetectionResultVO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist】的数据库操作Mapper
 * @createDate 2023-10-28 10:48:15
 * @Entity com.seu.platform.dao.entity.ProcessLinePictureHist
 */
public interface ProcessLinePictureHistMapper extends BaseMapper<ProcessLinePictureHist> {

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
     * @param ips  相机ip
     * @param time 时间
     * @return 检测结果
     */
    List<DetectionResultVO> getDetectionResult(List<String> ips, Date time);

    /**
     * 获取最近一次检测时间
     *
     * @return 最近一次时间
     */
    Date lastTime();

    /**
     * 获取月趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 趋势
     */
    List<TrendDTO> getMonthTrend(Date st, Date et);


    /**
     * 获取日趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 日趋势
     */
    List<TrendDTO> getDailyTrend(Date st, Date et);
}




