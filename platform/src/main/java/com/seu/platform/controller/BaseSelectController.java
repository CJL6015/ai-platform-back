package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.OptionItemVO;
import com.seu.platform.model.vo.SelectAllOptionVO;
import com.seu.platform.service.SelectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-09 16:09
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/select")
public class BaseSelectController {
    private final SelectService selectService;


    @GetMapping("/list")
    public Result<SelectAllOptionVO> getAllOptions() {
        SelectAllOptionVO allOptions = selectService.getAllOptions();
        return Result.success(allOptions);
    }

    @GetMapping("/line/list/{platId}")
    public Result<List<OptionItemVO>> getLinesOptions(@PathVariable Integer platId) {
        List<OptionItemVO> linesOptions = selectService.getLinesOptions(platId);
        return Result.success(linesOptions);
    }
}
