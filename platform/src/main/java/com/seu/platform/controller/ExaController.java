package com.seu.platform.controller;

import com.seu.platform.exa.ExaClient;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.PointValueVO;
import com.seu.platform.model.vo.TimeRange;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 20:38
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/exa")
public class ExaController {
    private final ExaClient exaClient;

    @GetMapping("/{point}")
    public Result<Float> getItemValue(@PathVariable String point) {
        Float value = exaClient.getValue(point);
        return Result.success(value);
    }

    @GetMapping("/history/{point}")
    public Result<PointValueVO> getHistory(@PathVariable String point, TimeRange timeRange) {
        long st = timeRange.getSt().getTime();
        long et = timeRange.getEt().getTime();
        RecordsFloat history = exaClient.getHistory(point, st, et);
        PointValueVO pointValueVO = PointValueVO.builder().values(history.getValues())
                .timestamps(history.getTimestamps()).build();
        return Result.success(pointValueVO);
    }
}
