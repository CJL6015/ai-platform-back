package com.seu.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.seu.platform.dao.entity.InspectionCfg;
import com.seu.platform.dao.service.InspectionCfgService;
import com.seu.platform.dao.service.InspectionHistoryService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.InspectionConfigVO;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-11 22:57
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inspection")
public class InspectionController {
    private final InspectionCfgService inspectionCfgService;

    private final InspectionHistoryService inspectionHistoryService;

    @GetMapping("/line/{id}")
    public Result<InspectionConfigVO> getInspectionConfig(@PathVariable Integer id) {
        InspectionConfigVO inspectionConfig = inspectionCfgService.getInspectionConfig(id);
        return Result.success(inspectionConfig);
    }

    @PatchMapping("/{id}")
    public Result<Boolean> updateInspectionConfig(@PathVariable Integer id, @RequestBody InspectionConfigVO vo) {
        InspectionCfg entity = new InspectionCfg();
        BeanUtil.copyProperties(vo, entity);
        entity.setId(id);
        boolean b = inspectionCfgService.updateById(entity);
        return Result.success(b);
    }

    @GetMapping("/history/line/{id}")
    public Result<InspectionHistoryDataVO> getInspectionHistory(@PathVariable Integer id, Long st, Long et) {
        InspectionHistoryDataVO inspectionHistoryValue = inspectionHistoryService.getInspectionHistoryValue(id, st, et);
        return Result.success(inspectionHistoryValue);
    }
}
