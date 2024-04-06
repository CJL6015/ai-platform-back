package com.seu.platform.controller;

import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.mapper.EquipmentRemainingLifeCfgMapper;
import com.seu.platform.dao.mapper.EquipmentRemainingUsefulLifeMapper;
import com.seu.platform.model.dto.LifeDTO;
import com.seu.platform.model.dto.LifeTrendDTO;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final EquipmentRemainingUsefulLifeMapper equipmentRemainingUsefulLifeMapper;


    @GetMapping("/{id}")
    public Result<List<LifeDTO>> getLife(@PathVariable Integer id) {
        List<LifeDTO> life = equipmentRemainingLifeCfgMapper.getLife(id);
        return Result.success(life);
    }

    @GetMapping("/trend/{id}")
    public Result getLifeTrend(@PathVariable Integer id) {
        List<LifeTrendDTO> lifeTrendDTO = equipmentRemainingUsefulLifeMapper.getLifeTrendDTO(id);
        List<String> time = new ArrayList<>();
        List<Integer> up = new ArrayList<>();
        List<Integer> low = new ArrayList<>();
        for (LifeTrendDTO trendDTO : lifeTrendDTO) {
            time.add(DateUtil.format(trendDTO.getTime(), "yyyy-MM-dd"));
            up.add(trendDTO.getUp());
            low.add(trendDTO.getLow());
        }
        Map<String, Object> result = new HashMap<>();
        result.put("time", time);
        result.put("up", up);
        result.put("low", low);
        return Result.success(result);
    }
}
