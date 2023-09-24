package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.model.vo.WarnConfigVO;

/**
 * @author 陈小黑
 * @description 针对表【warn_cfg(预警规则配置)】的数据库操作Service
 * @createDate 2023-09-11 22:24:59
 */
public interface WarnCfgService extends IService<WarnCfg> {

    /**
     * 获取定员阈值配置
     *
     * @param lineId 生产线id
     * @return vo
     */
    WarnConfigVO getWarnConfigVO(Integer lineId);

    /**
     * 更新配置
     *
     * @param lineId       生产线id
     * @param warnConfigVO 配置
     * @return 是否更新成功
     */
    boolean updateConfig(Integer lineId, WarnConfigVO warnConfigVO);
}
