package com.seu.platform.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.seu.platform.model.entity.Result;
import com.seu.platform.service.ReportService;
import com.seu.platform.task.ReportTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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

    private final ReportService reportService;

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
    public Result<String> getDayReport(Integer level, Integer lineId, Integer plantId, String time) {
        String path;
        if (level == 1) {
            path = prefix + "level1_day" + time + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_day" + plantId + time + ".docx";
        } else {
            path = prefix + "level3_day" + lineId + time + ".docx";
        }
        return Result.success(path);
    }


    @GetMapping("/month")
    public Result<String> getMonthReport(Integer level, Integer lineId, Integer plantId, String time) {
        String path;
        if (level == 1) {
            path = prefix + "level1_month" + time + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_month" + plantId + time + ".docx";
        } else {
            path = prefix + "level3_month" + lineId + time + ".docx";
        }
        return Result.success(path);
    }

    @GetMapping("/quarter")
    public Result<String> getQuarterReport(Integer level, Integer lineId, Integer plantId, String time) {
        String path;
        if (level == 1) {
            path = prefix + "level1_quarter" + time + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_quarter" + plantId + time + ".docx";
        } else {
            path = prefix + "level3_quarter" + lineId + time + ".docx";
        }
        return Result.success(path);
    }

    @GetMapping("/year")
    public Result<String> getYearReport(Integer level, Integer lineId, Integer plantId, String time) {
        String path;
        if (level == 1) {
            path = prefix + "level1_year" + time + ".docx";
        } else if (level == 2) {
            path = prefix + "level2_year" + plantId + time + ".docx";
        } else {
            path = prefix + "level3_year" + lineId + time + ".docx";
        }
        return Result.success(path);
    }

    @GetMapping("/level1")
    public Result createLevel1(String st, String et, String lastSt, String lastEt, String path) {
        reportService.createReportLevel1(DateUtil.parse(st), DateUtil.parse(et),
                DateUtil.parse(lastSt), DateUtil.parse(lastEt), reportDir + path);
        return Result.success();
    }

    @GetMapping("/level2/{id}")
    public Result createLevel2(String st, String et, String lastSt, String lastEt, String path, @PathVariable Integer id) {
        reportService.createReportLevel2(id, DateUtil.parse(st), DateUtil.parse(et),
                DateUtil.parse(lastSt), DateUtil.parse(lastEt), reportDir + path);
        return Result.success();
    }

    @GetMapping("/level3/{id}")
    public Result createLevel3(String st, String et, String lastSt, String lastEt, String path, @PathVariable Integer id) {
        reportService.createReportLevel3(id, DateUtil.parse(st), DateUtil.parse(et),
                DateUtil.parse(lastSt), DateUtil.parse(lastEt), path);
        reportService.createReportLevel3_1(id, DateUtil.parse(st), DateUtil.parse(et),
                DateUtil.parse(lastSt), DateUtil.parse(lastEt), path);
        reportService.createReportLevel3_2(id, DateUtil.parse(st), DateUtil.parse(et),
                DateUtil.parse(lastSt), DateUtil.parse(lastEt), reportDir + path);
        return Result.success();
    }


    @CrossOrigin(origins = {"http://localhost:5173", "http://localhost:80",
            "http://localhost:8088", "http://10.10.11.19:80"})
    @GetMapping("/download/{fileName}")
    public void downloadExport(HttpServletResponse response, @PathVariable String fileName) {
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        String path = reportDir + fileName;
        try {
            if (new File(path).exists()) {
                ServletOutputStream outputStream = response.getOutputStream();
                byte[] bytes = FileUtil.readBytes(path);
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            } else {
                log.error("文件不存在,path:{}", path);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("文档不存在,请生成历史后重试");
            }
        } catch (IOException e) {
            log.error("获取word文档异常:{}", fileName, e);
        }
    }

    @GetMapping("/create/report1")
    public Result<Boolean> createReport1() {
        reportTask.generateDayLevel1();
        reportTask.generateMonthLevel1();
        reportTask.generateQuarterLevel1();
        reportTask.generateYearLevel1();
        return Result.success();
    }

    @GetMapping("/create/report2")
    public Result<Boolean> createReport2() {
        reportTask.generateDayLevel2();
        reportTask.generateMonthLevel2();
        reportTask.generateQuarterLevel2();
        reportTask.generateYearLevel2();
        return Result.success();
    }

    @GetMapping("/create/report3")
    public Result<Boolean> createReport3() {
        log.info("开始生成三级报表");
        reportTask.generateDayLevel3();
        reportTask.generateMonthLevel3();
        reportTask.generateQuarterLevel3();
        reportTask.generateYearLevel3();
        return Result.success();
    }
}
