package com.seu.platform.controller;

import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.WarnConfigVO;
import com.seu.platform.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 陈小黑
 */
@RestController
@RequestMapping("/api/limit")
@RequiredArgsConstructor
public class WarnController {
    private final WarnCfgService warnCfgService;

    @GetMapping("/line/{id}")
    public Result<WarnConfigVO> getWarnConfig(@PathVariable Integer id) {
        WarnConfigVO warnConfigVO = warnCfgService.getWarnConfigVO(id);
        return Result.success(warnConfigVO);
    }

    @PatchMapping("/{id}")
    public Result<Boolean> updateWarnConfig(@PathVariable Integer id, @RequestBody WarnConfigVO warnConfigVO) {
        WarnCfg warnCfg = BeanUtil.convertBean(warnConfigVO, WarnCfg.class);
        warnCfg.setId(id);
        warnCfg.setModifyTime(new Date());
        boolean b = warnCfgService.updateById(warnCfg);
        return Result.success(b);
    }
}
