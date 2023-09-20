package com.seu.platform.dao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.mapper.PointCfgMapper;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.model.vo.PointStatisticVO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈小黑
 * @description 针对表【point_cfg(测点基表)】的数据库操作Service实现
 * @createDate 2023-09-11 22:24:42
 */
@Service
public class PointCfgServiceImpl extends ServiceImpl<PointCfgMapper, PointCfg>
        implements PointCfgService {
    @Override
    public Page<PointStatisticVO> getPointStatisticPage(Integer lineId, int pageNum, int pageSize) {
        Page<PointCfg> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId);
        Page<PointCfg> paged = page(page, queryWrapper);
        List<PointStatisticVO> records = paged.getRecords().stream()
                .map(this::convertToPointStatisticVO)
                .collect(Collectors.toList());
        Page<PointStatisticVO> result = new Page<>();
        result.setRecords(records);
        result.setTotal(paged.getTotal());
        result.setSize(paged.getSize());
        result.setCurrent(paged.getCurrent());
        return result;
    }

    @Override
    public List<PointStatisticVO> getPointStatistic(Integer lineId) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId);
        List<PointCfg> list = list(queryWrapper);
        return list.stream()
                .map(this::convertToPointStatisticVO)
                .collect(Collectors.toList());
    }

    private PointStatisticVO convertToPointStatisticVO(PointCfg pointCfg) {
        PointStatisticVO vo = new PointStatisticVO();
        BeanUtil.copyProperties(pointCfg, vo);
        return vo;
    }
}




