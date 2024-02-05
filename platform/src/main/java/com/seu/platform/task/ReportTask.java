package com.seu.platform.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.entity.ReportHistory;
import com.seu.platform.dao.mapper.ReportHistoryMapper;
import com.seu.platform.dao.service.ReportHistoryService;
import com.seu.platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 14:01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ReportTask {

    private final ResourceLoader resourceLoader;
    private final ReportService reportService;
    private final ReportHistoryService reportHistoryService;
    private final ReportHistoryMapper reportHistoryMapper;
    @Value("${static.report-dir}")
    private String reportDir;


    //    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 1000)
    public void generateLineReport() {
        Date lastTime = reportHistoryMapper.getLastTime(0);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成生产线报表,时间:{}", lastTime);
//            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime time = DateUtil.date();
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            for (int i = 1; i <= 4; i++) {
                String path = reportDir + "line_" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReport3(i, st, et, path);
                if (new File(path).exists()) {
                    log.info("生产线{}报表生成成功", i);
                }
            }
            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(0);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成报表");
        }
    }

    //    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 100)
    public void generatePlantReport() {
        Date lastTime = reportHistoryMapper.getLastTime(1);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成公司报表,时间:{}", lastTime);
//            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime time = DateUtil.date();
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            String path = reportDir + "report1" + DateUtil.format(st, "yyyyMM") + ".docx";
            reportService.createPlantReport(st, et, path);

            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(1);
                reportHistory.setTime(st);
//                reportHistoryService.save(reportHistory);
            }
        } else {
            log.info("不需要生成公司报表");
        }
    }

    //    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 100)
    public void generateInspectionReport() {
        Date lastTime = reportHistoryMapper.getLastTime(2);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成巡检报表,时间:{}", lastTime);
//            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime time = DateUtil.date();
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            String path = reportDir + "report2" + DateUtil.format(st, "yyyyMM") + ".docx";
            reportService.createReport2(st, et, path);
            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(2);
                reportHistory.setTime(st);
                reportHistoryService.save(reportHistory);
            }

        } else {
            log.info("不需要生成巡检报表");
        }
    }
}
