package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.CameraCfg;
import com.seu.platform.dao.entity.Plant;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.service.CameraCfgService;
import com.seu.platform.dao.service.PlantService;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.OptionItemVO;
import com.seu.platform.model.vo.SelectAllOptionVO;
import com.seu.platform.service.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    private final CameraCfgService cameraCfgService;

    private final PlantService plantService;


    @GetMapping("/list")
    public Result<SelectAllOptionVO> getAllOptions() {
        SelectAllOptionVO allOptions = selectService.getAllOptions();
        return Result.success(allOptions);
    }

    @GetMapping("/line/list/{platId}")
    public Result<List<OptionItemVO>> getLinesOptions(@RequestHeader("Authorization") String token,
                                                      @PathVariable Integer platId) {
        LambdaQueryWrapper<Plant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Plant::getId, platId);
        Plant plant = plantService.getOne(queryWrapper);
        if ((token.contains("jingmen") && plant.getName().contains("辽宁"))
                || (token.contains("liaoning") && plant.getName().contains("荆门"))) {
            return Result.fail("无权限,请联系管理员");
        }
        List<OptionItemVO> linesOptions = selectService.getLinesOptions(platId);
        return Result.success(linesOptions);
    }


    @GetMapping("/equipment/list/{lineId}")
    public Result<List<OptionItemVO>> getEquipments(@PathVariable Integer lineId) {
        List<OptionItemVO> equipments = selectService.getEquipments(lineId);
        return Result.success(equipments);
    }

    @GetMapping("/point/list/{lineId}")
    public Result<List<OptionItemVO>> getPoints(@PathVariable Integer lineId) {
        List<OptionItemVO> points = selectService.getPoints(lineId);
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

    @GetMapping("/camera/line/{lineId}")
    public Result<List<OptionItemVO>> getCameras(@PathVariable Integer lineId) {
        LambdaQueryWrapper<CameraCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CameraCfg::getLineId, lineId);
        List<CameraCfg> list = cameraCfgService.list(queryWrapper);
        List<OptionItemVO> itemVOList = list.stream().map(c -> OptionItemVO.builder()
                .id(c.getId())
                .name(c.getCameraDescription().trim())
                .build()).collect(Collectors.toList());
        itemVOList.add(0, new OptionItemVO(-1, "所有工序"));
        return Result.success(itemVOList);
    }
}
