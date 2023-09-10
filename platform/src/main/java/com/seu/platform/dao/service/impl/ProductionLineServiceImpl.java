package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.ProductionLine;
import com.seu.platform.dao.service.ProductionLineService;
import com.seu.platform.dao.mapper.ProductionLineMapper;
import org.springframework.stereotype.Service;

/**
* @author 陈小黑
* @description 针对表【production_line(生产线)】的数据库操作Service实现
* @createDate 2023-09-09 16:53:15
*/
@Service
public class ProductionLineServiceImpl extends ServiceImpl<ProductionLineMapper, ProductionLine>
    implements ProductionLineService{

}




