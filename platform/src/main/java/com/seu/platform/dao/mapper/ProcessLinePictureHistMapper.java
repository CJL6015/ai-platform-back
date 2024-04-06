package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.model.dto.*;
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

    List<DetectionResultVO> getSnapshotResult(List<String> ips);

    List<Date> getDetectionTime(Integer lineId, Date st, Date et);

    /**
     * 获取最近一次检测时间
     *
     * @return 最近一次时间
     */
    Date lastTime(Integer lineId);

    Date lastSnapshotTime(Integer lineId);

    /**
     * 获取月趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 趋势
     */
    List<TrendDTO> getMonthTrend(Integer lineId, Date st, Date et);


    /**
     * 获取日趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 日趋势
     */
    List<TrendDTO> getDailyTrend(Integer lineId, Date st, Date et);

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

    List<InspectionStatisticDTO> getLineInspection(Integer lineId, Date st, Date et);

    List<InspectionStatisticDTO> getLineInspectionHis(Integer lineId, Date st, Date et);

    List<InspectionStatisticDTO> getTotalInspection(Integer lineId, Date st, Date et);

    List<InspectionStatisticDTO> getTotalInspectionHis(Integer lineId, Date st, Date et);

    List<InspectionStatisticDTO> getLineInspectionHistory(Integer lineId, Date st, Date et);

    List<TrendDTO> getScoreDaily(Integer lineId, Date st, Date et);

    Integer getExceedCount(Integer lineId, Date st, Date et);

    Integer exceedCount(Integer lineId, Date st, Date et);

    List<LineInspection> getLast();

    List<LineInspection> getLast1();

    Boolean setInspectionMinute(String cameraIp, Date st, Date et, Integer code);

    Boolean setInspectionMinute1(String cameraIp, Date st, Date et);

    Date getFirstTime(String cameraIp);

    Date getNextTime(String cameraIp, Date st);

    Integer getCount(Integer lineId, String cameraIp, Date st, Date et);


    List<Integer> getCounts(Integer lineId, Date st, Date et);

    List<PeopleCounts> getPeopleCounts(Integer lineId, Date st, Date et);

    String getPicturePathString(String cameraIp);

    String getTopCamera(Integer lineId, Date st, Date et, String cameraIp);

    ExceedDTO getInspectionExceed(Integer lineId, Date st, Date et, String cameraIp);

    ExceedDTO getExceed(Integer lineId, Date st, Date et, String cameraIp);

}




