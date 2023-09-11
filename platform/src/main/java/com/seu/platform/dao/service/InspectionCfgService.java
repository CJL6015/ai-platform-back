package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.InspectionCfg;
import com.seu.platform.model.vo.InspectionConfigVO;

/**
 * @author 陈小黑
 * @description 针对表【inspection_cfg(巡检抓拍规则)】的数据库操作Service
 * @createDate 2023-09-11 22:17:04
 */
public interface InspectionCfgService extends IService<InspectionCfg> {
    /**
     * 获取巡检规则
     *
     * @param lineId 生产线id
     * @return 巡检规则
     */
    InspectionConfigVO getInspectionConfig(Integer lineId);
}
