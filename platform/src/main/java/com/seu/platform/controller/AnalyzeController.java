package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.ParamAnalyzeVO;
import com.seu.platform.service.AnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 21:08
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyze")
public class AnalyzeController {
    private final AnalyzeService analyzeService;

    @GetMapping("/param/{pointId}")
    public Result<ParamAnalyzeVO> getParamAnalyze(@PathVariable Integer pointId) {
        ParamAnalyzeVO paramAnalyze = analyzeService.getParamAnalyze(pointId);
        return Result.success(paramAnalyze);
    }
}
