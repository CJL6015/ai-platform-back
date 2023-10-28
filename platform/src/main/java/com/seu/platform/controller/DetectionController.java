package com.seu.platform.controller;

import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.DetectionResultVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-28 15:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/detection")
public class DetectionController {

    private final ProcessLinePictureHistService processLinePictureHistService;


    @GetMapping("/result")
    public Result<List<DetectionResultVO>> getDetectionResult(String ips, Date time) {
        String[] split = ips.split(",");
        List<DetectionResultVO> detectionResult = processLinePictureHistService.getDetectionResult(Arrays.asList(split), time);
        return Result.success(detectionResult);
    }
}
