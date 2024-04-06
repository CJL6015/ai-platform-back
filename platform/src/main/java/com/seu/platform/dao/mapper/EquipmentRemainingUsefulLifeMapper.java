package com.seu.platform.dao.mapper;

import com.seu.platform.dao.entity.EquipmentRemainingUsefulLife;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.model.dto.LifeTrendDTO;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【equipment_remaining_useful_life】的数据库操作Mapper
* @createDate 2024-04-04 21:17:29
* @Entity com.seu.platform.dao.entity.EquipmentRemainingUsefulLife
*/
public interface EquipmentRemainingUsefulLifeMapper extends BaseMapper<EquipmentRemainingUsefulLife> {

    List<LifeTrendDTO> getLifeTrendDTO(Integer id);

}




