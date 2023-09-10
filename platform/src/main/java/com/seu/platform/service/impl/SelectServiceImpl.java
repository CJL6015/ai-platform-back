package com.seu.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.Plant;
import com.seu.platform.dao.entity.ProductionLine;
import com.seu.platform.dao.service.PlantService;
import com.seu.platform.dao.service.ProductionLineService;
import com.seu.platform.model.vo.OptionItemVO;
import com.seu.platform.model.vo.SelectAllOptionVO;
import com.seu.platform.service.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-09 16:11
 */
@Service
@RequiredArgsConstructor
public class SelectServiceImpl implements SelectService {
    private final PlantService plantService;

    private final ProductionLineService productionLineService;

    @Override
    public SelectAllOptionVO getAllOptions() {
        List<Plant> plants = plantService.list();
        List<OptionItemVO> lineOptions = new ArrayList<>();
        List<OptionItemVO> plantOptions = plants.stream().map(plant -> OptionItemVO.builder().
                        id(plant.getId())
                        .name(plant.getName())
                        .build())
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(plants)) {
            LambdaQueryWrapper<ProductionLine> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ProductionLine::getPlantId, plants.get(0).getId());
            List<ProductionLine> productionLines = productionLineService.list(queryWrapper);
            lineOptions = productionLines.stream().map(line -> OptionItemVO.builder()
                            .id(line.getId())
                            .name(line.getName())
                            .build())
                    .collect(Collectors.toList());
        }


        return SelectAllOptionVO.builder()
                .plantOptions(plantOptions)
                .linesOptions(lineOptions)
                .build();
    }

    @Override
    public List<OptionItemVO> getLinesOptions(Integer plantId) {
        LambdaQueryWrapper<ProductionLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionLine::getPlantId, plantId);
        List<ProductionLine> productionLines = productionLineService.list(queryWrapper);
        return productionLines.stream().map(line -> OptionItemVO.builder()
                        .id(line.getId())
                        .name(line.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
