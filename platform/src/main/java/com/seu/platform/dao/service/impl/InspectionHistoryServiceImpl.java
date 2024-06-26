package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.InspectionHistory;
import com.seu.platform.dao.mapper.InspectionHistoryMapper;
import com.seu.platform.dao.service.InspectionHistoryService;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import com.seu.platform.model.vo.InspectionHistoryVO;
import com.seu.platform.model.vo.TimeValueChartVO;
import com.seu.platform.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈小黑
 * @description 针对表【inspection_history(巡检历史)】的数据库操作Service实现
 * @createDate 2023-09-11 22:17:07
 */
@Service
public class InspectionHistoryServiceImpl extends ServiceImpl<InspectionHistoryMapper, InspectionHistory>
        implements InspectionHistoryService {


    @Override
    public Integer getAllCount(Integer lineId, Date st, Date et) {
        return getBaseMapper().getAllExceededNum(lineId, st, et);
    }
}




