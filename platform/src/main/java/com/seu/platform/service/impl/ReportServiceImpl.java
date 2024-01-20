package com.seu.platform.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.*;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.dto.*;
import com.seu.platform.service.ReportService;
import com.seu.platform.util.WordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 13:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ResourceLoader resourceLoader;

    private final ProcessLinePictureHistMapper processLinePictureHistMapper;

    private final CameraCfgMapper cameraCfgMapper;

    private final PointInspectionHourMapper pointInspectionHourMapper;

    private final PointCfgService pointCfgService;

    private final WarnCfgService warnCfgService;

    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final LineStopRunStatisticHourMapper lineStopRunStatisticHourMapper;


    /**
     * 乳化线报表测点:水相流量,油相流量,粗乳器震动,基质泵压力,中转泵压力,酸泵流量,发泡剂流量,乳胶泵压力
     */
    private final String[] linePoint1 = {"KLRH3_DB2DD0", "KLRH3_DB2DD8", "KLRH3_DB6DBX2385", "KLRH3_DB2DD80",
            "KLRH3_DB2DD140", "KLRH3_DB2DD272", "KLRH3_DB2DD12", "KLRH3_DB2DD84"};

    private final Integer[] id1 = {35, 59, 205, 60, 42, 49, 39, 61};
    /**
     * 膨化线报表测点
     */
    private final String[] linePoint2 = {"KLPH_DB146DD54", "KLPH_DB147DD54", "KLPH_DB153DD54", "KLPH_DB125DD54", "", "KLPH_DB127DD54"};

    private final Integer[] id2 = {115, 116, 121, 100, 0, 102};

    @Override
    public String createReportMonth() {
        try {
            Resource resource = resourceLoader.getResource("classpath:word/inspection.docx");
            InputStream inputStream = resource.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);
            XWPFTable table = doc.getTables().get(0);
        } catch (IOException e) {
            log.error("生成word异常", e);
        }
        return null;
    }

    @Override
    public void createLineReport(Integer lineId, Date st, Date et, String path) {
        try {
            Resource resource = resourceLoader.getResource("classpath:word/line.docx");
            InputStream inputStream = resource.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);

            LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WarnCfg::getLineId, lineId);
            WarnCfg one = warnCfgService.getOne(queryWrapper);
            Double peopleScore = one.getPeopleScore();
            Double pointScore = one.getScore();
            Double highScore = one.getHighScore();
            XWPFTable peopleTable = doc.getTables().get(0);
            setPeopleTable(lineId, st, et, peopleTable, peopleScore);
            setPointTable(lineId, st, et, doc, highScore, pointScore);
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createPlantReport(Date st, Date et, String path) {
        try {
            Resource resource = resourceLoader.getResource("classpath:word/plant.docx");
            InputStream inputStream = resource.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);

            List<XWPFTable> tables = doc.getTables();
            //1.安全评分
            createPlantScoreTable(tables.get(0), st, et);
            //2.定期巡检
            createPlantPeopleExceedTable(tables.get(1), st, et);
            //3.成品仓库

            //4.综合评分
            createPlantSummaryTable(tables.get(3), tables.get(0), tables.get(1));
            //5.总体安全评分
            createPlantSummary(doc.getParagraphs());
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            log.error("生成公司月报异常", e);
        }
    }

    @Override
    public void createInspectionReport(Date st, Date et, String path) {
        try {
            Resource resource = resourceLoader.getResource("classpath:word/inspection.docx");
            InputStream inputStream = resource.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);

            List<XWPFTable> tables = doc.getTables();

            //1.乳化巡检
            createPeopleExceedTable(tables.get(0), st, et, ListUtil.of("192.168.50.69", "192.168.50.62", "192.168.50.65"
                    , "192.168.50.63", "192.168.50.76"));
            //2.膨化巡检
            createPeopleExceedTable(tables.get(1), st, et, ListUtil.of("192.168.50.97", "192.168.50.110", "192.168.50.109"
                    , "192.168.50.103"));
            //3.雷管线
            createPeopleExceedTable(tables.get(2), st, et, ListUtil.of("281k8i7192.zicp.fun:41842",
                    "281k8i7192.zicp.fun:28181"));

            //4.雷管线

            //5.炸药生产线主要设备

            //6.乳化线
            createPointExceedTable(tables.get(5), st, et, id1);
            //7.膨化线
            createPointExceedTable(tables.get(6), st, et, id2);

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);

        } catch (Exception e) {
            log.error("巡检月报异常", e);
        }
    }

    public void createPeopleExceedTable(XWPFTable table, Date st, Date et, List<String> ips) {
        int totalCount = 0;
        int exceedCount = 0;
        for (int i = 0; i < ips.size(); i++) {
            String ip = ips.get(i);
            XWPFTableRow row = table.getRow(i + 1);
            ExceedDTO exceed = processLinePictureHistMapper.getExceed(null, st, et, ip);
            if (exceed == null) {
                exceed = new ExceedDTO();
                exceed.setExceed(0);
                exceed.setTotal(24 * 30);
            }
            Integer total = exceed.getTotal();
            total = total == null || total == 0 ? 24 * 30 : total;
            Integer count = exceed.getExceed();
            count = count == null ? 0 : count;
            totalCount += total;
            exceedCount += count;
            row.getCell(2).setText(String.valueOf(total));
            row.getCell(3).setText(String.valueOf(count));
            row.getCell(4).setText(NumberUtil.formatPercent(1.0 * count / total, 2));
        }
        XWPFTableRow row = table.getRow(ips.size() + 1);
        row.getCell(2).setText(String.valueOf(totalCount));
        row.getCell(3).setText(String.valueOf(exceedCount));
        row.getCell(4).setText(NumberUtil.formatPercent(1.0 * exceedCount / totalCount, 2));
    }

    public void createPointExceedTable(XWPFTable table, Date st, Date et, Integer[] points) {
        List<PointReportDTO> pointReport = pointInspectionHourMapper.getPointReport(points);
        Map<Integer, PointReportDTO> map = pointReport.stream().collect(Collectors.toMap(PointReportDTO::getId, t -> t));
        int totalCount = 0;
        int exceedCount = 0;
        for (int i = 0; i < points.length; i++) {
            XWPFTableRow row = table.getRow(i + 1);
            PointReportDTO reportDTO = map.get(points[i]);
            if (reportDTO == null) {
                reportDTO = new PointReportDTO();
                reportDTO.setCount(24 * 30);
                reportDTO.setExceed(0);
            }
            Integer total = reportDTO.getCount();
            total = total == null || total == 0 ? 24 * 30 : total;
            Integer count = reportDTO.getExceed();
            count = count == null ? 0 : count;
            totalCount += total;
            exceedCount += count;
            row.getCell(2).setText(String.valueOf(reportDTO.getCount()));
            row.getCell(3).setText(String.valueOf(reportDTO.getExceed()));
            row.getCell(4).setText(NumberUtil.formatPercent(1.0 * reportDTO.getExceed() / reportDTO.getCount(), 2));
        }
        XWPFTableRow row = table.getRow(points.length + 1);
        row.getCell(2).setText(String.valueOf(totalCount));
        row.getCell(3).setText(String.valueOf(exceedCount));
        row.getCell(4).setText(NumberUtil.formatPercent(1.0 * exceedCount / totalCount, 2));
    }

    private void setPointTable(Integer lineId, Date st, Date et, XWPFDocument doc, Double highScore, Double pointScore) {
        List<PointExceedInspectionDTO> pointInspection = pointInspectionHourMapper.getPointInspection(lineId, st, et);
        XWPFTable table = doc.getTables().get(1);
        for (int i = 0; i < pointInspection.size(); i++) {
            PointExceedInspectionDTO dto = pointInspection.get(i);
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            row.getCell(1).setText(dto.getName().trim());
            Integer upUpCount = dto.getUpUpCount();
            row.getCell(2).setText(String.valueOf(upUpCount));
            Integer lowLowCount = dto.getLowLowCount();
            row.getCell(3).setText(String.valueOf(lowLowCount));
            Integer upCount = dto.getUpCount();
            row.getCell(4).setText(String.valueOf(upCount));
            Integer lowCount = dto.getLowCount();
            row.getCell(5).setText(String.valueOf(lowCount));
            String score = NumberUtil.formatPercent((upUpCount + lowLowCount) * highScore
                    + (upCount + lowCount) * pointScore, 2);
            row.getCell(6).setText(score);
        }
    }

    private void setPeopleTable(Integer lineId, Date st, Date et, XWPFTable peopleTable, Double peopleScore) {
        List<CountStatisticDTO> topProcess = processLinePictureHistMapper.getTopProcess(lineId, st, et);

        int totalExceedCount = 0;
        for (int i = 0; i < topProcess.size(); i++) {
            CountStatisticDTO countStatisticDTO = topProcess.get(i);
            XWPFTableRow row = peopleTable.createRow();
            row.getCell(0).setText(String.valueOf(i));
            row.getCell(1).setText(countStatisticDTO.getName().trim());
            Integer dtoCount = countStatisticDTO.getCount();
            row.getCell(2).setText(String.valueOf(dtoCount));
            totalExceedCount += dtoCount;
            String score = NumberUtil.formatPercent(peopleScore * dtoCount, 2);
            row.getCell(3).setText(score);
        }
        XWPFTableRow row = peopleTable.createRow();
        row.getCell(0).setText(String.valueOf(topProcess.size()));
        row.getCell(1).setText("全线");
        row.getCell(2).setText(String.valueOf(totalExceedCount));
        row.getCell(3).setText(NumberUtil.formatPercent(peopleScore * totalExceedCount, 2));
    }

    public void createPoint(XWPFTable table) {
        List<PointReportDTO> report1 = pointInspectionHourMapper.getPointReport(id1);
        Map<Integer, PointReportDTO> reportMap = report1.stream().collect(Collectors.toMap(PointReportDTO::getId, o -> o));
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PointCfg::getPointId, Arrays.asList(id1));
        List<PointCfg> points = pointCfgService.list(queryWrapper);
        Map<Integer, PointCfg> pointsMap = points.stream()
                .collect(Collectors.toMap(PointCfg::getPointId, o -> o));
        for (int i = 0; i < id1.length; i++) {
            XWPFTableRow row = table.createRow();

            PointReportDTO pointReportDTO = reportMap.get(id1[i]);
            Integer exceed = pointReportDTO.getExceed();
            exceed = exceed == null ? 0 : exceed;
            Integer count = pointReportDTO.getCount();
            String rate = NumberUtil.formatPercent(1.0 * exceed / count, 2);

            row.getCell(0).setText(String.valueOf(i + 1));
            row.getCell(1).setText(pointsMap.get(id1[i]).getDescription().trim());
            row.getCell(2).setText(String.valueOf(count));
            row.getCell(3).setText(String.valueOf(exceed));
            row.getCell(4).setText(String.valueOf(rate));
        }

        List<PointReportDTO> report2 = pointInspectionHourMapper.getPointReport(id2);

    }


    public void createPlantScoreTable(XWPFTable table, Date st, Date et) {
        Double lineScore1 = pointStatisticHourMapper.getLineScore(1, st, et);
        Double lineScore2 = pointStatisticHourMapper.getLineScore(2, st, et);
        Double lineScore3 = pointStatisticHourMapper.getLineScore(3, st, et);
        Double lineScore4 = pointStatisticHourMapper.getLineScore(4, st, et);

        lineScore1 = lineScore1 == null ? 0 : lineScore1;
        lineScore2 = lineScore2 == null ? 0 : lineScore2;
        lineScore3 = lineScore3 == null ? 0 : lineScore3;
        lineScore4 = lineScore4 == null ? 0 : lineScore4;

        Integer exceedCount1 = processLinePictureHistMapper.getExceedCount(1, st, et);
        Integer exceedCount2 = processLinePictureHistMapper.getExceedCount(2, st, et);
        Integer exceedCount3 = processLinePictureHistMapper.getExceedCount(3, st, et);
        Integer exceedCount4 = processLinePictureHistMapper.getExceedCount(4, st, et);

        exceedCount1 = exceedCount1 == null ? 0 : exceedCount1;
        exceedCount2 = exceedCount2 == null ? 0 : exceedCount2;
        exceedCount3 = exceedCount3 == null ? 0 : exceedCount3;
        exceedCount4 = exceedCount4 == null ? 0 : exceedCount4;

        List<Double> peopleScores = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WarnCfg::getLineId, i);
            WarnCfg one = warnCfgService.getOne(queryWrapper);
            if (one == null) {
                peopleScores.add(2.0);
            } else {
                Double peopleScore = one.getPeopleScore();
                if (peopleScore == null) {
                    peopleScore = 2.0;
                }
                peopleScores.add(peopleScore);
            }
        }
        Double score1 = 100 - lineScore1 - peopleScores.get(0) * exceedCount1;
        Double score2 = 100 - lineScore2 - peopleScores.get(1) * exceedCount2;
        Double score3 = 100 - lineScore3 - peopleScores.get(2) * exceedCount3;
        Double score4 = 100 - lineScore4 - peopleScores.get(3) * exceedCount4;
        String camera1 = processLinePictureHistMapper.getTopCamera(1, st, et, null);
        String camera2 = processLinePictureHistMapper.getTopCamera(2, st, et, null);
        String camera3 = processLinePictureHistMapper.getTopCamera(3, st, et, null);
        String camera4 = processLinePictureHistMapper.getTopCamera(4, st, et, null);

        List<String> topExceed1 = pointStatisticHourMapper.getTopExceed(1, st, et);
        List<String> topExceed2 = pointStatisticHourMapper.getTopExceed(2, st, et);
        List<String> topExceed3 = pointStatisticHourMapper.getTopExceed(3, st, et);
        List<String> topExceed4 = pointStatisticHourMapper.getTopExceed(4, st, et);

        Double[] scores = {score1, score2, score3, score4};
        String[] camera = {camera1, camera2, camera3, camera4};
        List<List<String>> tops = ListUtil.of(topExceed1, topExceed2, topExceed3, topExceed4);
        for (int i = 0; i < 4; i++) {
            XWPFTableRow row = table.getRow(i + 1);
            row.getCell(3).setText(NumberUtil.decimalFormat("#.##", scores[i]));
            row.getCell(4).setText(camera[i]);
            row.getCell(5).setText(tops.get(i).stream().map(String::trim).collect(Collectors.joining(",")));
        }
    }

    public void createPlantPeopleExceedTable(XWPFTable table, Date st, Date et) {
        for (int i = 1; i <= 4; i++) {
            ExceedDTO exceed = processLinePictureHistMapper.getExceed(i, st, et, null);
            Integer total = exceed.getTotal();
            total = total == null || total == 0 ? 24 * 30 : total;

            XWPFTableRow row = table.getRow(i);
            Integer exceed2 = exceed.getExceed();
            exceed2 = exceed2 == null ? 0 : exceed2;
            row.getCell(3).setText(NumberUtil.formatPercent(1.0 * exceed2 / total,
                    2));

            ExceedDTO exceed1 = pointInspectionHourMapper.getExceed(i, st, et);
            Integer exceed3 = exceed1.getExceed();
            exceed3 = exceed3 == null ? 0 : exceed3;
            Integer total1 = exceed1.getTotal();
            total1 = total1 == null || total1 == 0 ? 24 * 30 : total1;
            row.getCell(4).setText(NumberUtil.formatPercent(1.0 * exceed3 / total1,
                    2));
            RunTimeDTO runTimeByLineId = lineStopRunStatisticHourMapper.getRunTimeByLineId(i, st, et);
            if (runTimeByLineId == null) {
                runTimeByLineId = new RunTimeDTO();
                runTimeByLineId.setRun(DateUtil.betweenDay(st, et, false) * 24.0);
                runTimeByLineId.setStop(0.0);
            }
            String rate = NumberUtil.formatPercent(1.0 * runTimeByLineId.getRun() /
                    (runTimeByLineId.getRun() + runTimeByLineId.getStop()), 2);
            row.getCell(5).setText(rate);
        }
    }

    public void createPlantSummaryTable(XWPFTable table, XWPFTable table1, XWPFTable table2) {
        List<XWPFTableRow> rows1 = table1.getRows();
        double score1 = (Double.parseDouble(rows1.get(1).getCell(3).getText())
                + Double.parseDouble(rows1.get(2).getCell(3).getText())) / 2;
        table.getRow(1).getCell(2).setText(NumberUtil.decimalFormat("#.##", score1));

        double score2 = (Double.parseDouble(rows1.get(3).getCell(3).getText())
                + Double.parseDouble(rows1.get(4).getCell(3).getText())) / 2;
        table.getRow(2).getCell(2).setText(NumberUtil.decimalFormat("#.##", score2));

        List<XWPFTableRow> rows2 = table2.getRows();
        double rate1 = (Double.parseDouble(rows2.get(1).getCell(3).getText().replace("%", "").replace(",", ""))
                + Double.parseDouble(rows2.get(2).getCell(3).getText().replace("%", "").replace(",", ""))) / 2;
        double rate2 = (Double.parseDouble(rows2.get(1).getCell(4).getText().replace("%", ""))
                + Double.parseDouble(rows2.get(2).getCell(4).getText().replace("%", "").replace(",", ""))) / 2;
        double rate3 = (Double.parseDouble(rows2.get(1).getCell(5).getText().replace("%", "").replace(",", ""))
                + Double.parseDouble(rows2.get(2).getCell(5).getText().replace("%", "").replace(",", ""))) / 2;
        table.getRow(1).getCell(3).setText(NumberUtil.formatPercent(rate1, 2));
        table.getRow(1).getCell(4).setText(NumberUtil.formatPercent(rate2, 2));
        table.getRow(1).getCell(5).setText(NumberUtil.formatPercent(rate3, 2));

        double rate4 = (Double.parseDouble(rows2.get(3).getCell(3).getText().replace("%", ""))
                + Double.parseDouble(rows2.get(4).getCell(3).getText().replace("%", ""))) / 2;
        double rate5 = (Double.parseDouble(rows2.get(3).getCell(4).getText().replace("%", ""))
                + Double.parseDouble(rows2.get(4).getCell(4).getText().replace("%", ""))) / 2;
        double rate6 = (Double.parseDouble(rows2.get(3).getCell(5).getText().replace("%", ""))
                + Double.parseDouble(rows2.get(4).getCell(5).getText().replace("%", ""))) / 2;
        table.getRow(2).getCell(3).setText(NumberUtil.formatPercent(rate4, 2));
        table.getRow(2).getCell(4).setText(NumberUtil.formatPercent(rate5, 2));
        table.getRow(2).getCell(5).setText(NumberUtil.formatPercent(rate6, 2));
    }

    public void createPlantSummary(List<XWPFParagraph> paragraphs) {
        DateTime date = DateUtil.date();
        DateTime et = DateUtil.beginOfMonth(date);
        DateTime st = DateUtil.offsetMonth(et, -1);
        List<CountStatisticDTO> topEquipment = pointStatisticHourMapper.getTopEquipment(null, st, et);
        CountStatisticDTO dto = topEquipment.get(0);
        String maxEquipment = dto.getName().trim();
        Integer maxEquipmentCount = dto.getCount();
        String equipmentSummary = maxEquipment + " " + maxEquipmentCount + "次";

        List<CountStatisticDTO> topPoint = pointStatisticHourMapper.getTopPoint(null, st, et);
        CountStatisticDTO dto1 = topPoint.get(0);
        String maxPoint = dto1.getName().trim();
        Integer maxPointCount = dto1.getCount();
        String pointSummary = maxPoint + " " + maxPointCount + "次";

        List<CountStatisticDTO> topTime = pointStatisticHourMapper.getTopTime(null, st, et);
        CountStatisticDTO dto2 = topTime.get(0);
        String maxTime = dto2.getName().trim();
        Integer maxTimeCount = dto2.getCount();
        String timeSummary = maxTime + " " + maxTimeCount + "次";
        for (XWPFParagraph paragraph : paragraphs) {
            WordUtil.replaceTextInParagraph(paragraph, "equipment", equipmentSummary);
            WordUtil.replaceTextInParagraph(paragraph, "point", pointSummary);
            WordUtil.replaceTextInParagraph(paragraph, "time", timeSummary);
        }
    }
}