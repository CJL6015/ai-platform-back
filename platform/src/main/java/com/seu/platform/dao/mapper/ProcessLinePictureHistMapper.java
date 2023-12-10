package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.model.dto.CountStatisticDTO;
import com.seu.platform.model.dto.DetectionTrendDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.entity.LineInspection;
import com.seu.platform.model.vo.DetectionResultVO;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
     * @param ids   ids
     * @return 等待检测的图片
     */
    List<ProcessLinePictureHist> getPendingChecks(Integer count, Set<Long> ids);

    /**
     * 获取检测结果
     *
     * @param ips  相机ip
     * @param time 时间
     * @return 检测结果
     */
    List<DetectionResultVO> getDetectionResult(List<String> ips, Date time);

    List<Date> getDetectionTime(Date st, Date et);

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

    /**
     * 获取设备超限趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    List<DetectionTrendDTO> getPeopleTrend(Integer lineId, Date st, Date et);

    /**
     * 获取总趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    List<TrendDTO> getTotalTrend(Integer lineId, Date st, Date et);

    /**
     * 获取总趋势
     *
     * @param lineId 生产线id
     * @param st     开始时间
     * @param et     结束时间
     * @return 趋势
     */
    List<HourTrendDTO> getTimeOverrun(Integer lineId, Date st, Date et);

    List<CountStatisticDTO> getTopTime(Integer lineId, Date st, Date et);

    List<CountStatisticDTO> getTopProcess(Integer lineId, Date st, Date et);

    List<TrendDTO> getScoreDaily(Integer lineId, Date st, Date et);

    Integer getExceedCount(Integer lineId, Date st, Date et);

    Integer exceedCount(Integer lineId, Date st, Date et);

    List<LineInspection> getLast();

    Boolean setInspectionMinute(String cameraIp, Date st, Date et);

    Date getFirstTime(String cameraIp);

    Date getNextTime(String cameraIp, Date st);

    Integer getCount(String cameraIp, Date st, Date et);
}




