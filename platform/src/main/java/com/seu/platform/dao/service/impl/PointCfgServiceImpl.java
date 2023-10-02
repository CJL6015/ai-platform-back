package com.seu.platform.dao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.mapper.PointCfgMapper;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.exa.ExaClient;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.model.dto.PointStatisticDTO;
import com.seu.platform.model.vo.PointConfigVO;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.PointTrendVO;
import com.seu.platform.model.vo.TimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 陈小黑
 * @description 针对表【point_cfg(测点基表)】的数据库操作Service实现
 * @createDate 2023-09-24 21:45:47
 */
@Service
@RequiredArgsConstructor
public class PointCfgServiceImpl extends ServiceImpl<PointCfgMapper, PointCfg>
        implements PointCfgService {

    private final ExaClient exaClient;

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
    public List<PointStatisticVO> getPointStatistic(Integer lineId, TimeRange timeRange) {
        List<PointStatisticDTO> pointStatistic = getBaseMapper().getPointStatistic(lineId,
                timeRange.getSt(), timeRange.getEt());
        List<String> points = pointStatistic.stream()
                .map(PointStatisticDTO::getName)
                .collect(Collectors.toList());
        Float[] values = exaClient.getValues(points);
        List<PointStatisticVO> vos = new ArrayList<>();
        for (int i = 0; i < pointStatistic.size(); i++) {
            PointStatisticDTO dto = pointStatistic.get(i);
            PointStatisticVO vo = new PointStatisticVO();
            BeanUtil.copyProperties(dto, vo);
            vo.setValue(values[i]);
            vos.add(vo);
        }
        return vos;
    }

    @Override
    public List<PointConfigVO> getPointList(Integer lineId) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId);
        List<PointCfg> list = list(queryWrapper);
        return list.stream()
                .map(cfg ->
                        com.seu.platform.util.BeanUtil.convertBean(cfg, PointConfigVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PointTrendVO getPointTrend(String name, Long start, Long end) throws Exception {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(PointCfg::getName, name);
        PointCfg pointCfg = getOne(queryWrapper);
        RecordsFloat history = exaClient.getHistory(name, start, end);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> times = history.getTimestamps().stream()
                .map(dateFormat::format).collect(Collectors.toList());
        return PointTrendVO.builder()
                .name(pointCfg.getDescription() + "(" + pointCfg.getUnit() + ")")
                .lowerLimit(pointCfg.getLowerLimit())
                .lowerLowerLimit(pointCfg.getLowerLowerLimit())
                .upperLimit(pointCfg.getUpperLimit())
                .upperUpperLimit(pointCfg.getUpperUpperLimit())
                .times(times)
                .value(history.getValues())
                .build();

    }

    private PointStatisticVO convertToPointStatisticVO(PointCfg pointCfg) {
        PointStatisticVO vo = new PointStatisticVO();
        BeanUtil.copyProperties(pointCfg, vo);
        return vo;
    }
}




