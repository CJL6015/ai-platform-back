package com.seu.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.PointStatisticService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.StatisticVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @GetMapping("/{id}")
    public Result<Page<PointStatisticVO>> getPointStatistic(@PathVariable Integer id, int pageNum, int pageSize) {
        Page<PointStatisticVO> pointStatisticPage = pointCfgService.getPointStatisticPage(id, pageNum, pageSize);
        return Result.success(pointStatisticPage);
    }
}
