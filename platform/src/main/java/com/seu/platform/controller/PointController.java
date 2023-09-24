package com.seu.platform.controller;

import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.PointConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 15:59
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/point")
public class PointController {

    private final PointCfgService pointCfgService;

    @GetMapping("/line/{lineId}")
    public Result<List<PointConfigVO>> getPointList(@PathVariable Integer lineId) {
        List<PointConfigVO> pointList = pointCfgService.getPointList(lineId);
        return Result.success(pointList);
    }

}
