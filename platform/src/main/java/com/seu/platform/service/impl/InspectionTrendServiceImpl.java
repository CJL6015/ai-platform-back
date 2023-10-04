package com.seu.platform.service.impl;

import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.model.vo.TrendVO;
import com.seu.platform.service.InspectionTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:43
 */
@Service
@RequiredArgsConstructor
public class InspectionTrendServiceImpl implements InspectionTrendService {
    private final PointStatisticHourService pointStatisticHourService;
    @Override
    public TrendVO<String, Integer> getPointInspectionTrendMonth(Integer lineId, Date st, Date et) {
        return null;
    }
}
