package com.seu.platform.dao.mapper;

import com.seu.platform.dao.entity.EquipmentRemainingLifeCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.model.dto.LifeDTO;
import com.seu.platform.model.dto.LifeTrendDTO;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【equipment_remaining_life_cfg】的数据库操作Mapper
* @createDate 2024-04-04 21:27:53
* @Entity com.seu.platform.dao.entity.EquipmentRemainingLifeCfg
*/
public interface EquipmentRemainingLifeCfgMapper extends BaseMapper<EquipmentRemainingLifeCfg> {
    List<LifeDTO> getLife(Integer id);

}




