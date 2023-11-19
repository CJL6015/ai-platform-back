package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.OptionItemVO;
import com.seu.platform.model.vo.SelectAllOptionVO;
import com.seu.platform.service.SelectService;
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
 * @date 2023-09-09 16:09
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/select")
public class BaseSelectController {
    private final SelectService selectService;

    private final PointCfgService pointCfgService;


    @GetMapping("/list")
    public Result<SelectAllOptionVO> getAllOptions() {
        SelectAllOptionVO allOptions = selectService.getAllOptions();
        return Result.success(allOptions);
    }

    @GetMapping("/line/list/{platId}")
    public Result<List<OptionItemVO>> getLinesOptions(@PathVariable Integer platId) {
        List<OptionItemVO> linesOptions = selectService.getLinesOptions(platId);
        return Result.success(linesOptions);
    }


    @GetMapping("/equipment/list/{lineId}")
    public Result<List<OptionItemVO>> getEquipments(@PathVariable Integer lineId) {
        List<OptionItemVO> equipments = selectService.getEquipments(lineId);
        return Result.success(equipments);
    }

    @GetMapping("/point/list")
    public Result<List<OptionItemVO>> getPoints() {
        List<OptionItemVO> points = selectService.getPoints();
        return Result.success(points);
    }

    @GetMapping("/point/line/{lineId}")
    public Result<List<String>> getPointNames(@PathVariable Integer lineId) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId);
        List<PointCfg> list = pointCfgService.list(queryWrapper);
        List<String> names = list.stream().map(t -> t.getDescription().trim()).collect(Collectors.toList());
        return Result.success(names);
    }
}
