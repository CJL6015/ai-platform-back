package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.model.entity.LineInspection;
import com.seu.platform.model.vo.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
     * @param ids   ids
     * @return 等待检测的图片
     */
    List<ProcessLinePictureHist> getPendingChecks(Integer count, Set<Long> ids);


    /**
     * 获取检测结果
     *
     * @param ips  摄像机ip
     * @param time 时间
     * @return 检测结果
     */
    List<DetectionResultVO> getDetectionResult(List<String> ips, Date time);

    List<String> getTimes();

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

    /**
     * 查询设备超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    DetectionTrendVO getPeopleTrend(Integer lineId, Date st, Date et);

    /**
     * 查询总超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    BenchmarkTrendVO getBenchmarkTrend(Integer lineId, Date st, Date et);

    List<LineInspection> getLast();

    Integer exceedCount(Integer lineId, Date st, Date et);


    Boolean setInspectionMinute(String cameraIp, Date st, Date et);

    Date getFirstTime(String cameraIp);

    Date getNextTime(String cameraIp, Date st);
}
