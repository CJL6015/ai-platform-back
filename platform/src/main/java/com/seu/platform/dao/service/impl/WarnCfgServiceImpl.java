package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.WarnCfgMapper;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.vo.WarnConfigVO;
import com.seu.platform.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 陈小黑
 * @description 针对表【warn_cfg(预警规则配置)】的数据库操作Service实现
 * @createDate 2023-09-11 22:24:59
 */
@Service
public class WarnCfgServiceImpl extends ServiceImpl<WarnCfgMapper, WarnCfg>
        implements WarnCfgService {

    @Override
    public WarnConfigVO getWarnConfigVO(Integer lineId) {
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, lineId);
        WarnCfg warnCfg = getOne(queryWrapper);
        return BeanUtil.convertBean(warnCfg, WarnConfigVO.class);
    }

    @Override
    public boolean updateConfig(Integer lineId, WarnConfigVO warnConfigVO) {
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, lineId);
        int count = count(queryWrapper);
        WarnCfg entity = com.seu.platform.util.BeanUtil.convertBean(warnConfigVO, WarnCfg.class);
        entity.setLineId(lineId);
        entity.setModifyTime(new Date());
        boolean result;
        if (count < 1) {
            result = save(entity);
        } else {
            result = update(entity, queryWrapper);
        }
        return result;
    }
}




