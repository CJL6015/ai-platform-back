package com.seu.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.FreezeLog;
import com.seu.platform.dao.mapper.ProcessLinePictureHist1Mapper;
import com.seu.platform.dao.service.FreezeLogService;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import com.seu.platform.model.vo.TrendVO;
import com.seu.platform.service.InspectionTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:43
 */
@Service
@RequiredArgsConstructor
public class InspectionTrendServiceImpl implements InspectionTrendService {
    private static final int STEP = 60 * 60 * 1000;

    private static final double HOUR = 60 * 60 * 1000.0;

    private final PointStatisticHourService pointStatisticHourService;

    private final ProcessLinePictureHist1Mapper processLinePictureHist1Mapper;

    private final FreezeLogService freezeLogService;

    @Override
    public TrendVO<String, Integer> getPointInspectionTrendMonth(Integer lineId, Date st, Date et) {
        return null;
    }

    @Override
    public InspectionHistoryDataVO getInspectionHistory(Integer lineId, Date st, Date et) {
        LambdaQueryWrapper<FreezeLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FreezeLog::getLineId, lineId)
                .ge(FreezeLog::getStartTime, st)
                .le(FreezeLog::getStartTime, et);
        List<FreezeLog> list = freezeLogService.list(queryWrapper);


        return null;
    }
}
