package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.EquipmentCfg;
import com.seu.platform.dao.service.EquipmentCfgService;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 20:21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentCfgService equipmentCfgService;


    @GetMapping("/name/{lineId}")
    public Result<List<String>> listName(@PathVariable Integer lineId) {
        LambdaQueryWrapper<EquipmentCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentCfg::getLineId, lineId)
                .orderBy(Boolean.TRUE, Boolean.TRUE, EquipmentCfg::getId);
        List<EquipmentCfg> list = equipmentCfgService.list(queryWrapper);
        List<String> names = list.stream().map(t -> t.getEquipmentDescription().trim()).collect(Collectors.toList());
        return Result.success(names);
    }
}
