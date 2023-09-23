package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointStatisticHour;
import com.seu.platform.dao.service.PointStatisticHourService;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.TimeRange;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【point_statistic_hour】的数据库操作Service实现
* @createDate 2023-09-23 16:14:08
*/
@Service
public class PointStatisticHourServiceImpl extends ServiceImpl<PointStatisticHourMapper, PointStatisticHour>
    implements PointStatisticHourService{
    @Override
    public List<PointStatisticVO> getPointStatistic(Integer lineId, TimeRange timeRange) {
        return null;
    }
}




