package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.seu.platform.dao.entity.StorehouseHist;
import com.seu.platform.dao.service.StorehouseHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.StorehouseHistVO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-24 21:37
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/storehouse")
public class StorehouseController {
    private static final int LIMIT = 200;
    private final StorehouseHistService service;

    @GetMapping("/history")
    public Result<List<StorehouseHistVO>> getList(@DateTimeFormat(pattern = "yyyy-MM")
                                                  @JsonFormat(pattern = "yyyy-MM", timezone = "GMT+8")
                                                  Date time) {
        DateTime st = DateUtil.beginOfMonth(time);
        DateTime et = DateUtil.endOfMonth(time);
        LambdaQueryWrapper<StorehouseHist> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.lt(StorehouseHist::getStoreRecordTime, et)
                .ge(StorehouseHist::getStoreRecordTime, st)
                .orderByAsc(Boolean.TRUE, StorehouseHist::getStoreRecordTime);
        List<StorehouseHist> list = service.list(queryWrapper);
        List<StorehouseHistVO> collect = list.stream().map(t -> StorehouseHistVO.builder()
                .time(t.getStoreRecordTime())
                .limit(LIMIT)
                .stock(t.getStoreCount())
                .exceed(t.getStoreCount() > LIMIT ? "是" : "否")
                .build()).collect(Collectors.toList());
        return Result.success(collect);

    }
}
