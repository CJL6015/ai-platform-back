package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.EquipmentCfg;
import com.seu.platform.model.dto.EquipmentTrendDTO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【equipment_cfg】的数据库操作Mapper
 * @createDate 2023-10-04 14:43:01
 * @Entity com.seu.platform.dao.entity.EquipmentCfg
 */
public interface EquipmentCfgMapper extends BaseMapper<EquipmentCfg> {
    /**
     * 获取设备超限趋势
     *
     * @param st 开始时间
     * @param et 结束时间
     * @return 趋势
     */
    List<EquipmentTrendDTO> getEquipmentTrend(Date st, Date et);
}




