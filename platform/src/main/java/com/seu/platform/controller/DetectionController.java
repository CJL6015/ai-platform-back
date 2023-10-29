package com.seu.platform.controller;

import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.DetectionResultVO;
import com.seu.platform.model.vo.TimeRange;
import com.seu.platform.model.vo.TrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        Random random = new Random();
        String[] path = {"http://114.55.245.123/api/static/images/装车工序.jpg", "http://114.55.245.123/api/static/images/制药工序.png"};
        detectionResult.forEach(r -> r.setDetectionPicturePath(path[random.nextInt(2)]));
        return Result.success(detectionResult);
    }

    @GetMapping("/trend/month")
    public Result<TrendVO<String, Integer>> getTrendMonth(TimeRange timeRange) {
        TrendVO<String, Integer> trendMonth = processLinePictureHistService.getTrendMonth(timeRange);
        return Result.success(trendMonth);
    }

    @GetMapping("/trend/daily")
    public Result<TrendVO<String, Integer>> getTrendDaily(TimeRange timeRange) {
        TrendVO<String, Integer> trendMonth = processLinePictureHistService.getTrendDaily(timeRange);
        return Result.success(trendMonth);
    }
}
