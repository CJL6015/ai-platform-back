package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.RelationVO;
import com.seu.platform.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 15:19
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rule")
public class RuleController {
    private final RuleService ruleService;

    @GetMapping("/equipment/{lineId}")
    public Result<RelationVO> getEquipment(@PathVariable Integer lineId, Double limit) {
        RelationVO supportEquipment = ruleService.getSupportEquipment(lineId, limit);
        return Result.success(supportEquipment);
    }

    @GetMapping("/point/{lineId}")
    public Result<RelationVO> getPoint(@PathVariable Integer lineId, Double limit) {
        RelationVO supportPoint = ruleService.getSupportPoint(lineId, limit);
        return Result.success(supportPoint);
    }

}
