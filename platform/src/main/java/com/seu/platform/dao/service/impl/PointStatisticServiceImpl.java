package com.seu.platform.dao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointStatistic;
import com.seu.platform.dao.mapper.PointStatisticMapper;
import com.seu.platform.dao.service.PointStatisticService;
import com.seu.platform.model.vo.StatisticVO;
import org.springframework.stereotype.Service;

/**
 * @author 陈小黑
 * @description 针对表【point_statistic(点号统计)】的数据库操作Service实现
 * @createDate 2023-09-11 22:33:34
 */
@Service
public class PointStatisticServiceImpl extends ServiceImpl<PointStatisticMapper, PointStatistic>
        implements PointStatisticService {

    @Override
    public StatisticVO getOneVo(Integer lineId) {
        LambdaQueryWrapper<PointStatistic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointStatistic::getLineId, lineId)
                .orderByAsc(PointStatistic::getCreateTime);
        PointStatistic entity = getOne(queryWrapper);
        StatisticVO vo = new StatisticVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}




