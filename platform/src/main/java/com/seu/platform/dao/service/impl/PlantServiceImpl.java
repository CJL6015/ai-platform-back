package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.Plant;
import com.seu.platform.dao.service.PlantService;
import com.seu.platform.dao.mapper.PlantMapper;
import org.springframework.stereotype.Service;

/**
* @author 陈小黑
* @description 针对表【plant(公司表)】的数据库操作Service实现
* @createDate 2023-09-09 16:53:09
*/
@Service
public class PlantServiceImpl extends ServiceImpl<PlantMapper, Plant>
    implements PlantService{

}




