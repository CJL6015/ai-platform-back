package com.seu.platform.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.seu.platform.dao.entity.FreezeLog;
import com.seu.platform.dao.entity.InspectionCfg;
import com.seu.platform.dao.entity.InspectionHistory;
import com.seu.platform.dao.entity.ProcessLinePictureHist1;
import com.seu.platform.dao.service.FreezeLogService;
import com.seu.platform.dao.service.InspectionCfgService;
import com.seu.platform.dao.service.InspectionHistoryService;
import com.seu.platform.dao.service.ProcessLinePictureHist1Service;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.InspectionConfigVO;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import com.seu.platform.model.vo.TimeRange;
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

    private final FreezeLogService freezeLogService;

    private final ProcessLinePictureHist1Service processLinePictureHist1Service;

    @GetMapping("/line/{id}")
    public Result<InspectionConfigVO> getInspectionConfig(@PathVariable Integer id) {
        InspectionConfigVO inspectionConfig = inspectionCfgService.getInspectionConfig(id);
        return Result.success(inspectionConfig);
    }

    @PostMapping()
    public Result<Boolean> save(@RequestBody InspectionConfigVO vo) {
        InspectionCfg entity = new InspectionCfg();
        BeanUtil.copyProperties(vo, entity);
        boolean save = inspectionCfgService.save(entity);
        return Result.success(save);
    }

    @PatchMapping()
    public Result<Boolean> updateInspectionConfig(@RequestBody InspectionConfigVO vo) {
        boolean b = inspectionCfgService.updateInspectionConfig(vo);
        return Result.success(b);
    }

    @GetMapping("/history/line/{id}")
    public Result<InspectionHistoryDataVO> getInspectionHistory(@PathVariable Integer id,
                                                                TimeRange timeRange) {
        InspectionHistoryDataVO inspectionHistoryValue = freezeLogService.getInspectionHistoryFreeze(id,
                timeRange.getSt(), timeRange.getEt());
        Integer allCount = inspectionHistoryService.getAllCount(id,
                timeRange.getSt(), timeRange.getEt());
        inspectionHistoryValue.setUnfreezeCount(allCount - inspectionHistoryValue.getFreezeCount());
        return Result.success(inspectionHistoryValue);
    }

    @PostMapping("/freeze/{lineId}")
    public Result<Boolean> freeze(@PathVariable Integer lineId, @RequestBody TimeRange time) {
        FreezeLog freezeLog = new FreezeLog();
        freezeLog.setLineId(lineId);
        freezeLog.setStartTime(time.getSt());
        freezeLog.setEndTime(time.getEt());
        boolean save = freezeLogService.save(freezeLog);
        LambdaUpdateWrapper<ProcessLinePictureHist1> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.ge(ProcessLinePictureHist1::getUpdateTime, time.getSt())
                .le(ProcessLinePictureHist1::getUpdateTime, time.getEt());
        updateWrapper.set(ProcessLinePictureHist1::getFreeze, 1);
        processLinePictureHist1Service.update(updateWrapper);
        return Result.success(save);
    }

    @DeleteMapping("/unfreeze/{id}")
    public Result<Boolean> unfreeze(@PathVariable Integer id) {
        boolean b = freezeLogService.removeById(id);
        return Result.success(b);
    }
}
