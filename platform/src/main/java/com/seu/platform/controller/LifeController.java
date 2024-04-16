package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.seu.platform.dao.mapper.EquipmentRemainingLifeCfgMapper;
import com.seu.platform.dao.mapper.EquipmentRemainingUsefulLifeMapper;
import com.seu.platform.model.dto.LifeDTO;
import com.seu.platform.model.dto.LifeTrendDTO;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
        for (LifeDTO lifeDTO : life) {
            Double design = lifeDTO.getDesign();
            double design1 = design / 365;
            lifeDTO.setDesign(Double.parseDouble(NumberUtil.decimalFormat("#.##", design1)));
            double v = lifeDTO.getLow() / 365;
            double v1 = lifeDTO.getUp() / 365;
            lifeDTO.setRemain(Double.parseDouble(NumberUtil.decimalFormat("#.##", v)) + "-"
                    + Double.parseDouble(NumberUtil.decimalFormat("#.##", v1)));
            Date dtoTime = lifeDTO.getTime();
            DateTime low = DateUtil.offsetDay(dtoTime, lifeDTO.getLow().intValue());
            DateTime up = DateUtil.offsetDay(dtoTime, lifeDTO.getUp().intValue());
            String expire = DateUtil.format(low, "yyyy-MM-dd") + "-" + DateUtil.format(up, "yyyy-MM-dd");
            lifeDTO.setExpire(expire);
        }
        return Result.success(life);
    }

    @GetMapping("/trend/{id}")
    public Result getLifeTrend(@PathVariable Integer id,
                               @RequestParam Long st,
                               @RequestParam Long et) {
        List<LifeTrendDTO> lifeTrendDTO = equipmentRemainingUsefulLifeMapper.getLifeTrendDTO(id
                , DateUtil.date(st)
                , DateUtil.date(et));
        List<String> time = new ArrayList<>();
        List<Double> up = new ArrayList<>();
        List<Double> low = new ArrayList<>();
        for (LifeTrendDTO trendDTO : lifeTrendDTO) {
            time.add(DateUtil.format(trendDTO.getTime(), "yyyy-MM-dd"));
            up.add(Double.parseDouble(NumberUtil.decimalFormat("#.##", trendDTO.getUp() / 365)));
            low.add(Double.parseDouble(NumberUtil.decimalFormat("#.##", trendDTO.getLow() / 365)));
        }
        Map<String, Object> result = new HashMap<>();
        result.put("time", time);
        result.put("up", up);
        result.put("low", low);
        return Result.success(result);
    }
}
