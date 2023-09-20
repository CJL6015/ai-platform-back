package com.seu.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.PointStatisticService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.StatisticVO;
import com.seu.platform.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-11 22:29
 */
@RestController
@RequestMapping("/api/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final PointStatisticService pointStatisticService;

    private final PointCfgService pointCfgService;


    @GetMapping("/{id}")
    public Result<StatisticVO> getStatistic(@PathVariable Integer id) {
        StatisticVO vo = pointStatisticService.getOneVo(id);
        return Result.success(vo);
    }


    @GetMapping("/point/page/{id}")
    public Result<Page<PointStatisticVO>> getPointStatistic(@PathVariable Integer id, int pageNum, int pageSize) {
        Page<PointStatisticVO> pointStatisticPage = pointCfgService.getPointStatisticPage(id, pageNum, pageSize);
        return Result.success(pointStatisticPage);
    }

    @GetMapping("/point/{id}")
    public Result<List<PointStatisticVO>> getPoint(@PathVariable Integer id) {
        List<PointStatisticVO> pointStatistic = pointCfgService.getPointStatistic(id);
        return Result.success(pointStatistic);
    }

    @PatchMapping("/point")
    public Result<Boolean> updatePointThreshold(@RequestBody PointStatisticVO vo) {
        PointCfg pointCfg = BeanUtil.convertBean(vo, PointCfg.class);
        pointCfg.setModifyTime(new Date());
        boolean b = pointCfgService.updateById(pointCfg);
        return Result.success(b);

    }
}
