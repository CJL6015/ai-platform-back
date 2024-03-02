package com.seu.platform.task;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.entity.Plant;
import com.seu.platform.dao.entity.ReportHistory;
import com.seu.platform.dao.mapper.ReportHistoryMapper;
import com.seu.platform.dao.service.PlantService;
import com.seu.platform.dao.service.ReportHistoryService;
import com.seu.platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.List;

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
    private final PlantService plantService;

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

    /**
     * 一级月报
     */
    @Scheduled(cron = "0 0 * * * *")
    public void generateDayLevel1() {
        Date lastTime = reportHistoryMapper.getLastTime(0);
        DateTime yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        if (lastTime == null) {
            lastTime = DateUtil.offsetDay(yesterday, -1);
        }
        if (lastTime.before(yesterday)) {
            log.info("开始生成一级日报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetDay(lastTime, 1);
            DateTime st = DateUtil.beginOfDay(time);
            DateTime et = DateUtil.endOfDay(time);
            DateTime lastSt = DateUtil.offsetDay(st, -1);
            DateTime lastEt = DateUtil.offsetDay(et, -1);
            String path = reportDir + "level1_day" + DateUtil.format(st, "yyyyMMdd") + ".docx";
            reportService.createReportLevel1(st, et, lastSt, lastEt, path);

            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(0);
                reportHistory.setTime(st);
                reportHistoryService.save(reportHistory);
            }
        } else {
            log.info("不需要生成一级日表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateMonthLevel1() {
        Date lastTime = reportHistoryMapper.getLastTime(1);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成一级月报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -1);
            DateTime lastEt = DateUtil.offsetMonth(et, -1);
            String path = reportDir + "level1_month" + DateUtil.format(st, "yyyyMM") + ".docx";
            reportService.createReportLevel1(st, et, lastSt, lastEt, path);

            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(1);
                reportHistory.setTime(st);
                reportHistoryService.save(reportHistory);
            }
        } else {
            log.info("不需要生成一级月表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateQuarterLevel1() {
        Date lastTime = reportHistoryMapper.getLastTime(2);
        DateTime quarter = DateUtil.beginOfQuarter(DateUtil.date());
        DateTime lastQuarter = DateUtil.offsetMonth(quarter, -3);
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastQuarter, -3);
        }
        if (lastTime.before(lastQuarter)) {
            log.info("开始生成一级季报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 3);
            DateTime st = DateUtil.beginOfQuarter(time);
            DateTime et = DateUtil.endOfQuarter(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -3);
            DateTime lastEt = DateUtil.offsetMonth(et, -3);
            String path = reportDir + "level1_quarter" + DateUtil.format(st, "yyyyMM") + ".docx";
            reportService.createReportLevel1(st, et, lastSt, lastEt, path);

            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(2);
                reportHistory.setTime(st);
                reportHistoryService.save(reportHistory);
            }
        } else {
            log.info("不需要生成一级季表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateYearLevel1() {
        Date lastTime = reportHistoryMapper.getLastTime(3);
        DateTime year = DateUtil.beginOfYear(DateUtil.date());
        DateTime lastYear = DateUtil.offset(year, DateField.YEAR, -1);
        if (lastTime == null) {
            lastTime = DateUtil.offset(lastYear, DateField.YEAR, -1);
        }
        if (lastTime.before(lastYear)) {
            log.info("开始生成一级年报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offset(lastTime, DateField.YEAR, 1);
            DateTime st = DateUtil.beginOfYear(time);
            DateTime et = DateUtil.endOfYear(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -12);
            DateTime lastEt = DateUtil.offsetMonth(et, -12);
            String path = reportDir + "level1_year" + DateUtil.format(st, "yyyyMM") + ".docx";
            reportService.createReportLevel1(st, et, lastSt, lastEt, path);

            if (new File(path).exists()) {
                ReportHistory reportHistory = new ReportHistory();
                reportHistory.setType(3);
                reportHistory.setTime(st);
                reportHistoryService.save(reportHistory);
            }
        } else {
            log.info("不需要生成一级年表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateDayLevel2() {
        Date lastTime = reportHistoryMapper.getLastTime(4);
        DateTime yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        if (lastTime == null) {
            lastTime = DateUtil.offsetDay(yesterday, -1);
        }
        if (lastTime.before(yesterday)) {
            log.info("开始生成二级日报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetDay(lastTime, 1);
            DateTime st = DateUtil.beginOfDay(time);
            DateTime et = DateUtil.endOfDay(time);
            DateTime lastSt = DateUtil.offsetDay(st, -1);
            DateTime lastEt = DateUtil.offsetDay(et, -1);
            List<Plant> list = plantService.list();
            for (Plant plant : list) {
                Integer plantId = plant.getId();
                String path = reportDir + "level2_day" + plantId + DateUtil.format(st, "yyyyMMdd") + ".docx";
                reportService.createReportLevel2(plantId, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("二级日报生成成功:{}", plant);
                }
            }

            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(4);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成二级日表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateMonthLevel2() {
        Date lastTime = reportHistoryMapper.getLastTime(5);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成二级月报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -1);
            DateTime lastEt = DateUtil.offsetMonth(et, -1);
            List<Plant> list = plantService.list();
            for (Plant plant : list) {
                Integer plantId = plant.getId();
                String path = reportDir + "level2_month" + plantId + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel2(plantId, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("二级月报生成成功:{}", plant);
                }
            }

            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(5);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成二级月表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateQuarterLevel2() {
        Date lastTime = reportHistoryMapper.getLastTime(6);
        DateTime quarter = DateUtil.beginOfQuarter(DateUtil.date());
        DateTime lastQuarter = DateUtil.offsetMonth(quarter, -3);
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastQuarter, -3);
        }
        if (lastTime.before(lastQuarter)) {
            log.info("开始生成二级季报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 3);
            DateTime st = DateUtil.beginOfQuarter(time);
            DateTime et = DateUtil.endOfQuarter(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -3);
            DateTime lastEt = DateUtil.offsetMonth(et, -3);

            List<Plant> list = plantService.list();
            for (Plant plant : list) {
                Integer plantId = plant.getId();
                String path = reportDir + "level2_quarter" + plantId + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel2(plantId, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("二级季报生成成功:{}", plant);
                }
            }
            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(6);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成二级季表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateYearLevel2() {
        Date lastTime = reportHistoryMapper.getLastTime(7);
        DateTime year = DateUtil.beginOfYear(DateUtil.date());
        DateTime lastYear = DateUtil.offsetMonth(year, -12);
        if (lastTime == null) {
            lastTime = DateUtil.offset(lastYear, DateField.YEAR, -1);
        }
        if (lastTime.before(lastYear)) {
            log.info("开始生成二级年报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offset(lastTime, DateField.YEAR, 1);
            DateTime st = DateUtil.beginOfYear(time);
            DateTime et = DateUtil.endOfYear(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -12);
            DateTime lastEt = DateUtil.offsetMonth(et, -12);
            List<Plant> list = plantService.list();
            for (Plant plant : list) {
                Integer plantId = plant.getId();
                String path = reportDir + "level2_year" + plantId + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel2(plantId, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("二级年报生成成功:{}", plant);
                }
            }
            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(7);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成二级季表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateDayLevel3() {
        Date lastTime = reportHistoryMapper.getLastTime(8);
        DateTime yesterday = DateUtil.beginOfDay(DateUtil.yesterday());
        if (lastTime == null) {
            lastTime = DateUtil.offsetDay(yesterday, -1);
        }
        if (lastTime.before(yesterday)) {
            log.info("开始生成三级日报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetDay(lastTime, 1);
            DateTime st = DateUtil.beginOfDay(time);
            DateTime et = DateUtil.endOfDay(time);
            DateTime lastSt = DateUtil.offsetDay(st, -1);
            DateTime lastEt = DateUtil.offsetDay(et, -1);
            for (int i = 1; i <= 2; i++) {
                String path = reportDir + "level3_day" + i + DateUtil.format(st, "yyyyMMdd") + ".docx";
                if (new File(path).exists()) {
                    log.info("文件存在跳过");
                    continue;
                }
                reportService.createReportLevel3(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级日报生成成功:{}", i);
                }
            }

            for (int i = 3; i <= 4; i++) {
                String path = reportDir + "level3_day" + i + DateUtil.format(st, "yyyyMMdd") + ".docx";
                reportService.createReportLevel3_1(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级日报生成成功:{}", i);
                }
            }

            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(8);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成三级日表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateMonthLevel3() {
        Date lastTime = reportHistoryMapper.getLastTime(9);
        DateTime lastMonth = DateUtil.beginOfMonth(DateUtil.lastMonth());
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastMonth, -1);
        }
        if (lastTime.before(lastMonth)) {
            log.info("开始生成三级月报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 1);
            DateTime st = DateUtil.beginOfMonth(time);
            DateTime et = DateUtil.endOfMonth(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -1);
            DateTime lastEt = DateUtil.offsetMonth(et, -1);
            for (int i = 1; i <= 2; i++) {
                String path = reportDir + "level3_month" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                if (new File(path).exists()) {
                    log.info("文件存在跳过");
                    continue;
                }
                reportService.createReportLevel3(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级月报生成成功:{}", i);
                }
            }

            for (int i = 3; i <= 4; i++) {
                String path = reportDir + "level3_month" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel3_1(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级月报生成成功:{}", i);
                }
            }

            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(9);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成三级月表");
        }
    }

    //    @Scheduled(fixedRate = 100)
    @Scheduled(cron = "0 0 * * * *")
    public void generateQuarterLevel3() {
        Date lastTime = reportHistoryMapper.getLastTime(10);
        DateTime quarter = DateUtil.beginOfQuarter(DateUtil.date());
        DateTime lastQuarter = DateUtil.offsetMonth(quarter, -3);
        if (lastTime == null) {
            lastTime = DateUtil.offsetMonth(lastQuarter, -3);
        }
        if (lastTime.before(lastQuarter)) {
            log.info("开始生成三级季报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offsetMonth(lastTime, 3);
            DateTime st = DateUtil.beginOfQuarter(time);
            DateTime et = DateUtil.endOfQuarter(time);
            DateTime lastSt = DateUtil.offsetMonth(st, -3);
            DateTime lastEt = DateUtil.offsetMonth(et, -3);

            for (int i = 1; i <= 2; i++) {
                String path = reportDir + "level3_quarter" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                if (new File(path).exists()) {
                    log.info("文件存在跳过");
                    continue;
                }
                reportService.createReportLevel3(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级季报生成成功:{}", i);
                }
            }

            for (int i = 3; i <= 4; i++) {
                String path = reportDir + "level3_quarter" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel3_1(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级季报生成成功:{}", i);
                }
            }
            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(10);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成三级季表");
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void generateYearLevel3() {
        Date lastTime = reportHistoryMapper.getLastTime(11);
        DateTime year = DateUtil.beginOfYear(DateUtil.date());
        DateTime lastYear = DateUtil.offset(year, DateField.YEAR, -1);
        if (lastTime == null) {
            lastTime = DateUtil.offset(lastYear, DateField.YEAR, -1);
        }
        if (lastTime.before(lastYear)) {
            log.info("开始生成三级年报报表,时间:{}", lastTime);
            DateTime time = DateUtil.offset(lastTime, DateField.YEAR, 1);
            DateTime st = DateUtil.beginOfYear(time);
            DateTime et = DateUtil.endOfYear(time);
            DateTime lastSt = DateUtil.offset(st, DateField.YEAR, -1);
            DateTime lastEt = DateUtil.offset(et, DateField.YEAR, -1);
            List<Plant> list = plantService.list();
            for (int i = 1; i <= 2; i++) {
                String path = reportDir + "level3_year" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel3(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级年报生成成功:{}", i);
                }
            }
            for (int i = 3; i <= 4; i++) {
                String path = reportDir + "level3_year" + i + DateUtil.format(st, "yyyyMM") + ".docx";
                reportService.createReportLevel3_1(i, st, et, lastSt, lastEt, path);
                if (new File(path).exists()) {
                    log.info("三级年报生成成功:{}", i);
                }
            }
            ReportHistory reportHistory = new ReportHistory();
            reportHistory.setType(11);
            reportHistory.setTime(st);
            reportHistoryService.save(reportHistory);
        } else {
            log.info("不需要生成三级年表");
        }
    }
}
