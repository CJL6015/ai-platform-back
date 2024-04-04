package com.seu.platform.controller;

import com.seu.platform.dao.mapper.EquipmentRemainingLifeCfgMapper;
import com.seu.platform.model.dto.LifeDTO;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-04-04 21:04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/life")
public class LifeController {
    private final EquipmentRemainingLifeCfgMapper equipmentRemainingLifeCfgMapper;


    @GetMapping("/{id}")
    public Result<List<LifeDTO>> getLife(@PathVariable Integer id) {
        List<LifeDTO> life = equipmentRemainingLifeCfgMapper.getLife(id);
        return Result.success(life);
    }
}
