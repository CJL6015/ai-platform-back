package com.seu.platform.dao.mapper;

import com.seu.platform.dao.entity.CameraCfg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author 陈小黑
* @description 针对表【camera_cfg】的数据库操作Mapper
* @createDate 2023-11-22 22:07:18
* @Entity com.seu.platform.dao.entity.CameraCfg
*/
public interface CameraCfgMapper extends BaseMapper<CameraCfg> {

    List<String> getNames(Integer lineId);

}




