package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.WarnConfigVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈小黑
 */
@RestController
@RequestMapping("/api/limit")
@RequiredArgsConstructor
public class WarnController {
    private final WarnCfgService warnCfgService;

    private final PointCfgService pointCfgService;

    @GetMapping("/line/{id}")
    public Result<WarnConfigVO> getWarnConfig(@PathVariable Integer id) {
        WarnConfigVO warnConfigVO = warnCfgService.getWarnConfigVO(id);
        return Result.success(warnConfigVO);
    }

    @PatchMapping("/line/{id}")
    public Result<Boolean> updateWarnConfig(@PathVariable Integer id, @RequestBody WarnConfigVO warnConfigVO) {
        boolean b = warnCfgService.updateConfig(id, warnConfigVO);
        if (id != 3 && id != 4) {
            LambdaUpdateWrapper<PointCfg> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PointCfg::getScore, warnConfigVO.getScore())
                    .set(PointCfg::getHighScore, warnConfigVO.getHighScore())
                    .eq(PointCfg::getLineId, id);
            boolean b1 = pointCfgService.update(updateWrapper);
            return Result.success(b && b1);
        }
        return Result.success(b);

    }
}
