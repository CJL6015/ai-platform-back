package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.seu.platform.model.entity.Result;
import com.seu.platform.task.ReportTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 15:08
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {

    private final ResourceLoader resourceLoader;

    private final ReportTask reportTask;

    @Value("${static.word-prefix}")
    private String prefix;

    @Value("${static.report-dir}")
    private String reportDir;

    @GetMapping("/report1")
    public Result<String> getReport1() {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        return Result.success(prefix + "report1" + DateUtil.format(time, "yyyyMM") + ".docx");
    }

    @GetMapping("/report2")
    public Result<String> getReport2() {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        return Result.success(prefix + "report2" + DateUtil.format(time, "yyyyMM") + ".docx");
    }

    @GetMapping("/report3/{lineId}")
    public Result<String> getReport3(@PathVariable Integer lineId) {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        return Result.success(prefix + "line_" + lineId + DateUtil.format(time, "yyyyMM") + ".docx");
    }

    @GetMapping("/day")
    public Result<String> getDayReport(Integer level, Integer lineId, Integer plantId) {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        String path;
        if (level == 1) {
            path = prefix + "level1_day" + DateUtil.format(time, "yyyyMMdd") + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_day" + plantId + DateUtil.format(time, "yyyyMM") + ".docx";
        } else {
            path = prefix + "level3_day" + lineId + DateUtil.format(time, "yyyyMM") + ".docx";
        }
        return Result.success(path);
    }


    @GetMapping("/month")
    public Result<String> getMonthReport(Integer level, Integer lineId, Integer plantId) {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        String path;
        if (level == 1) {
            path = prefix + "level1_month" + DateUtil.format(time, "yyyyMMdd") + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_month" + plantId + DateUtil.format(time, "yyyyMM") + ".docx";
        } else {
            path = prefix + "level3_month" + lineId + DateUtil.format(time, "yyyyMM") + ".docx";
        }
        return Result.success(path);
    }

    @GetMapping("/quarter")
    public Result<String> getQuarterReport(Integer level, Integer lineId, Integer plantId) {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        String path;
        if (level == 1) {
            path = prefix + "level1_quarter" + DateUtil.format(time, "yyyyMMdd") + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_quarter" + plantId + DateUtil.format(time, "yyyyMM") + ".docx";
        } else {
            path = prefix + "level3_quarter" + lineId + DateUtil.format(time, "yyyyMM") + ".docx";
        }
        return Result.success(path);
    }

    @GetMapping("/year")
    public Result<String> getYearReport(Integer level, Integer lineId, Integer plantId) {
        DateTime time = DateUtil.beginOfMonth(DateUtil.date());
        String path;
        if (level == 1) {
            path = prefix + "level1_year" + DateUtil.format(time, "yyyyMM") + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_year" + plantId + DateUtil.format(time, "yyyyMM") + ".docx";
        } else {
            path = prefix + "level3_year" + lineId + DateUtil.format(time, "yyyyMM") + ".docx";
        }
        return Result.success(path);
    }


    @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:80",
            "http://localhost:8088", "http://10.10.11.19:80"})
    @GetMapping("/download/{fileName}")
    public void downloadExport(HttpServletResponse response, @PathVariable String fileName) {
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        String path = reportDir + fileName;
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileUtil.readBytes(path);
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("获取word文档异常:{}", fileName, e);
        }
    }

    @GetMapping("/create/report1")
    public Result<Boolean> createReport1() {
        reportTask.generatePlantReport();
        return Result.success();
    }

    @GetMapping("/create/report2")
    public Result<Boolean> createReport2() {
        reportTask.generateInspectionReport();
        return Result.success();
    }

    @GetMapping("/create/report3")
    public Result<Boolean> createReport3() {
        reportTask.generateLineReport();
        return Result.success();
    }
}
