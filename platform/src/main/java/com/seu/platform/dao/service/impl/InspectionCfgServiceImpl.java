package com.seu.platform.dao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.InspectionCfg;
import com.seu.platform.dao.mapper.InspectionCfgMapper;
import com.seu.platform.dao.service.InspectionCfgService;
import com.seu.platform.model.vo.InspectionConfigVO;
import org.springframework.stereotype.Service;

/**
 * @author 陈小黑
 * @description 针对表【inspection_cfg(巡检抓拍规则)】的数据库操作Service实现
 * @createDate 2023-09-11 22:17:04
 */
@Service
public class InspectionCfgServiceImpl extends ServiceImpl<InspectionCfgMapper, InspectionCfg>
        implements InspectionCfgService {
    @Override
    public InspectionConfigVO getInspectionConfig(Integer lineId) {
        LambdaQueryWrapper<InspectionCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionCfg::getLineId, lineId);
        InspectionCfg one = getOne(queryWrapper);
        return convertToInspectionConfigVO(one);
    }

    private InspectionConfigVO convertToInspectionConfigVO(InspectionCfg entity) {
        InspectionConfigVO vo = new InspectionConfigVO();
        BeanUtil.copyProperties(entity, vo);
        return vo;
    }
}




