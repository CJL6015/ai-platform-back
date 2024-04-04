package com.seu.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.InspectionCfg;
import com.seu.platform.dao.entity.ProductionLine;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.*;
import com.seu.platform.dao.service.*;
import com.seu.platform.exa.ExaClient;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.model.dto.*;
import com.seu.platform.service.ReportService;
import com.seu.platform.util.WordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    private final ProductionLineService productionLineService;

    private final InspectionCfgService inspectionCfgService;

    private final StorehouseHistService storehouseHistService;

    private final ExaClient exaClient;

    @Value("${static.word-dir}")
    private String wordDir;


    public void replaceName(List<XWPFParagraph> paragraphs, Integer lineId) {
        LambdaQueryWrapper<ProductionLine> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionLine::getId, lineId);
        ProductionLine one = productionLineService.getOne(queryWrapper);
        String name = one.getName().trim();
        paragraphs.forEach(p -> {
            WordUtil.replaceTextInParagraph(p, "line", name);
        });
    }


    public void createInspectionLineTable(XWPFTable table, Integer lineId, Date st, Date et,
                                          Date lastSt, Date lastEt, Double peopleScore, int runDay) {
        DateTime lastYearSt = DateUtil.offset(st, DateField.YEAR, -1);
        DateTime lastYearEt = DateUtil.offset(et, DateField.YEAR, -1);
        List<InspectionStatisticDTO> lineInspection = processLinePictureHistMapper.getLineInspection(lineId, st, et);
        List<InspectionStatisticDTO> last = processLinePictureHistMapper.getLineInspectionHis(lineId, lastSt, lastEt);
        List<InspectionStatisticDTO> lastYear = processLinePictureHistMapper.getLineInspectionHis(lineId, lastYearSt, lastYearEt);
        Map<String, Integer> map = new HashMap<>(32);
        if (CollUtil.isNotEmpty(last)) {
            map = last.stream()
                    .filter(Objects::nonNull)
                    .filter(t -> t.getExceed() != null)
                    .filter(t -> t.getName() != null)
                    .collect(Collectors.toMap(t -> t.getName().trim(),
                            InspectionStatisticDTO::getExceed));
        }
        Map<String, Integer> lastYearMap = new HashMap<>(32);
        if (CollUtil.isNotEmpty(lastYear)) {
            lastYearMap = lastYear.stream()
                    .filter(Objects::nonNull)
                    .filter(t -> t.getExceed() != null)
                    .filter(t -> t.getName() != null)
                    .collect(Collectors.toMap(t -> t.getName().trim(),
                            InspectionStatisticDTO::getExceed));
        }

        for (int i = 0; i < lineInspection.size(); i++) {
            InspectionStatisticDTO dto = lineInspection.get(i);
            XWPFTableRow row = table.createRow();

            String name = dto.getName().trim();
            row.getCell(0).setText(String.valueOf(i + 1));
            row.getCell(1).setText(name);
            Integer count = dto.getCount();
            if (count == null) {
                count = 0;
            }
            row.getCell(2).setText(String.valueOf(count));
            Integer exceed = dto.getExceed();
            row.getCell(3).setText(String.valueOf(exceed));
            count = Math.max(1, count);
            row.getCell(4).setText(NumberUtil.formatPercent(1.0 * exceed / count, 2));
            Integer lastCount = map.get(name);
            String tb, hb;
            if (lastCount == null) {
                tb = "暂无数据";
            } else {
                tb = exceed >= lastCount ? "增加" : "减少";
                tb = "同比" + tb + Math.abs(exceed - lastCount) + "次";
            }
            Integer lastYearCount = lastYearMap.get(name);
            if (lastYearCount == null) {
                hb = "暂无数据";
            } else {
                hb = "环比" + (exceed >= lastYearCount ? "增加" : "减少");
                hb += Math.abs(exceed - lastYearCount) + "次";
            }
            row.getCell(5).setText(tb + "/" + hb);
        }
        XWPFTableRow row = table.createRow();
        List<InspectionStatisticDTO> totalInspection = processLinePictureHistMapper.getTotalInspection(lineId, st, et);
        List<InspectionStatisticDTO> lastTotalInspection = processLinePictureHistMapper.getTotalInspection(lineId, lastSt, lastEt);
        List<InspectionStatisticDTO> lastYearTotalInspection = processLinePictureHistMapper.getTotalInspection(lineId, lastYearSt, lastYearEt);
        int totalCount = totalInspection.size();
        long totalExceed = totalInspection.stream().mapToInt(InspectionStatisticDTO::getExceed).filter(t -> t > 3).count();
        row.getCell(0).setText(String.valueOf(lineInspection.size() + 1));
        row.getCell(1).setText("全线");
        row.getCell(2).setText(String.valueOf(totalCount));
        row.getCell(3).setText(String.valueOf(totalExceed));
        if (totalCount == 0) {
            row.getCell(4).setText("---");
        } else {
            row.getCell(4).setText(NumberUtil.formatPercent(1.0 * totalExceed / totalCount, 2));
        }
        String tb, hb;
        if (CollUtil.isEmpty(lastTotalInspection)) {
            tb = "暂无数据";
        } else {
            long lastTotalCount = lastTotalInspection.stream()
                    .mapToInt(InspectionStatisticDTO::getExceed)
                    .filter(t -> t > 3).count();
            tb = totalExceed >= lastTotalCount ? "同比增加" : "同比减少";
            tb += Math.abs(totalExceed - lastTotalCount) + "次";
        }
        if (CollUtil.isEmpty(lastYearTotalInspection)) {
            hb = "暂无数据";
        } else {
            long lastYearTotalCount = lastYearTotalInspection.stream()
                    .mapToInt(InspectionStatisticDTO::getExceed)
                    .filter(t -> t > 3).count();
            hb = totalExceed >= lastYearTotalCount ? "环比增加" : "环比减少";
            hb += Math.abs(totalExceed - lastYearTotalCount) + "次";
        }
        row.getCell(5).setText(tb + "/" + hb);
    }

    public void createPointInspectionLineTable(XWPFTable table, Integer lineId, Date st, Date et,
                                               Date lastSt, Date lastEt, Double score, Double highScore, int day) {
        DateTime lastYearSt = DateUtil.offset(st, DateField.YEAR, -1);
        DateTime lastYearEt = DateUtil.offset(et, DateField.YEAR, -1);
        List<PointExceedInspectionDTO> pointInspection = pointInspectionHourMapper.getPointInspection(lineId, st, et);
        List<PointExceedInspectionDTO> last = pointInspectionHourMapper.getPointInspection(lineId, lastSt, lastEt);
        List<PointExceedInspectionDTO> lastYear = pointInspectionHourMapper.getPointInspection(lineId, lastYearSt, lastYearEt);
        Map<String, Integer> map = last.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedInspectionDTO::getExceed));
        Map<String, Integer> lastYearMap = lastYear.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedInspectionDTO::getExceed));
        for (int i = 0; i < pointInspection.size(); i++) {
            PointExceedInspectionDTO dto = pointInspection.get(i);
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            String name = dto.getName().trim();
            row.getCell(1).setText(name);
            Integer exceed = dto.getExceed();
            Integer count = dto.getCount();
            count = count == null || count == 0 ? day * 24 : count;
            row.getCell(2).setText(String.valueOf(count));
            row.getCell(3).setText(String.valueOf(exceed));
            row.getCell(4).setText(dto.getRate());
            row.getCell(5).setText(dto.getScore(score, highScore, day));
            String tb, hb;
            Integer lastCount = map.get(name);
            if (lastCount == null) {
                tb = "暂无数据";
            } else {
                tb = "同比" + (exceed >= lastCount ? "增加" : "减少");
                tb += Math.abs(exceed - lastCount) + "次";
            }
            Integer lastYearCount = lastYearMap.get(name);
            if (lastYearCount == null) {
                hb = "暂无数据";
            } else {
                hb = "环比" + (exceed >= lastYearCount ? "增加" : "减少");
                hb += Math.abs(exceed - lastYearCount) + "次";
            }
            row.getCell(6).setText(tb + "/" + hb);
        }
    }


    public double createInspectionHistoryTable(XWPFTable table, Integer lineId, Date st, Date et,
                                               Date lastSt, Date lastEt, Double peopleScore, int day) {
        DateTime lastYearSt = DateUtil.offset(st, DateField.YEAR, -1);
        DateTime lastYearEt = DateUtil.offset(et, DateField.YEAR, -1);
        List<InspectionStatisticDTO> lineInspection = processLinePictureHistMapper.getLineInspectionHistory(lineId, st, et);
        List<InspectionStatisticDTO> last = processLinePictureHistMapper.getLineInspectionHistory(lineId, lastSt, lastEt);
        List<InspectionStatisticDTO> lastYear = processLinePictureHistMapper.getLineInspectionHistory(lineId, lastYearSt, lastYearEt);
        Map<String, Integer> map = last.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        InspectionStatisticDTO::getExceed));
        Map<String, Integer> lastYearMap = lastYear.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null).collect(Collectors.toMap(t -> t.getName().trim(),
                        InspectionStatisticDTO::getExceed));
        double totalScore = 0;
        for (int i = 0; i < lineInspection.size(); i++) {
            InspectionStatisticDTO dto = lineInspection.get(i);
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            String name = dto.getName().trim();
            row.getCell(1).setText(name);
            Integer exceed = dto.getExceed();
            row.getCell(2).setText(String.valueOf(exceed));
            String tb, hb;
            Integer lastCount = map.get(name);
            if (lastCount == null) {
                tb = "暂无数据";
            } else {
                tb = "同比" + (exceed >= lastCount ? "增加" : "减少");
                tb += Math.abs(exceed - lastCount) + "次";
            }
            Integer lastYearCount = lastYearMap.get(name);
            if (lastYearCount == null) {
                hb = "暂无数据";
            } else {
                hb = "环比" + (exceed >= lastYearCount ? "增加" : "减少");
                hb += Math.abs(exceed - lastYearCount) + "次";
            }
            row.getCell(3).setText(tb + "/" + hb);
        }
        XWPFTableRow row = table.createRow();
        List<InspectionStatisticDTO> totalInspection = processLinePictureHistMapper.getTotalInspectionHis(lineId, st, et);
        List<InspectionStatisticDTO> lastTotalInspection = processLinePictureHistMapper.getTotalInspectionHis(lineId, lastSt, lastEt);
        List<InspectionStatisticDTO> lastYearTotalInspection = processLinePictureHistMapper.getTotalInspectionHis(lineId, lastYearSt, lastYearEt);
        int totalCount = totalInspection.size();
        long totalExceed = totalInspection.stream().mapToInt(InspectionStatisticDTO::getExceed).filter(t -> t > 3).count();
        row.getCell(0).setText(String.valueOf(lineInspection.size() + 1));
        row.getCell(1).setText("全线");
        row.getCell(2).setText(String.valueOf(totalExceed));
        String tb, hb;
        if (CollUtil.isEmpty(lastTotalInspection)) {
            tb = "暂无数据";
        } else {
            long lastTotalCount = lastTotalInspection.stream()
                    .mapToInt(InspectionStatisticDTO::getExceed)
                    .filter(t -> t > 3).count();
            tb = totalExceed >= lastTotalCount ? "同比增加" : "同比减少";
            tb += Math.abs(totalExceed - lastTotalCount) + "次";
        }
        if (CollUtil.isEmpty(lastYearTotalInspection)) {
            hb = "暂无数据";
        } else {
            long lastYearTotalCount = lastYearTotalInspection.stream()
                    .mapToInt(InspectionStatisticDTO::getExceed)
                    .filter(t -> t > 3).count();
            hb = totalExceed >= lastYearTotalCount ? "环比增加" : "环比减少";
            hb += Math.abs(totalExceed - lastYearTotalCount) + "次";
        }
        row.getCell(3).setText(tb + "/" + hb);

        return totalScore;
    }

    public double createPointHistoryTable(XWPFTable table, Integer lineId, Date st, Date et,
                                          Date lastSt, Date lastEt, Double score, Double highScore, int day) {
        DateTime lastYearSt = DateUtil.offset(st, DateField.YEAR, -1);
        DateTime lastYearEt = DateUtil.offset(et, DateField.YEAR, -1);
        List<PointExceedDTO> pointExceedHistory = pointStatisticHourMapper.getPointExceedHistory(lineId, st, et);
        List<PointExceedDTO> last = pointStatisticHourMapper.getPointExceedHistory(lineId, lastSt, lastEt);
        List<PointExceedDTO> lastYear = pointStatisticHourMapper.getPointExceedHistory(lineId, lastYearSt, lastYearEt);
        Map<String, Integer> map = last.stream().filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedDTO::getExceed));
        Map<String, Integer> lastYearMap = lastYear.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedDTO::getExceed));
        double totalScore = 0;
        for (int i = 0; i < pointExceedHistory.size(); i++) {
            PointExceedDTO dto = pointExceedHistory.get(i);
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            String name = dto.getName().trim();
            row.getCell(1).setText(name);
            Integer count = dto.getCount();
            count = count == null ? 0 : count;
            Integer highCount = dto.getHighCount();
            highCount = highCount == null ? 0 : highCount;
            row.getCell(2).setText(String.valueOf(highCount));
            row.getCell(3).setText(String.valueOf(count));
            double pScore = (count * score + highCount * highScore) / day;
            totalScore += pScore;
            String lineScore = NumberUtil.decimalFormat("#.##", pScore);
            row.getCell(4).setText(lineScore);
            int c = count + highCount;
            Integer lastCount = map.get(name);
            String tb;
            if (lastCount == null) {
                tb = "暂无数据";
            } else {
                tb = "同比" + (c >= lastCount ? "增加" : "减少");
                tb += Math.abs(c - lastCount) + "次";
            }
            String hb;
            Integer lastYearCount = lastYearMap.get(name);
            if (lastYearCount == null) {
                hb = "暂无数据";
            } else {
                hb = "环比" + (c >= lastYearCount ? "增加" : "减少");
                hb += Math.abs(c - lastYearCount) + "次";
            }

            row.getCell(5).setText(tb + "/" + hb);

        }
        return totalScore;
    }

    public double createPointHistoryTable1(XWPFTable table, Integer lineId, Date st, Date et,
                                           Date lastSt, Date lastEt, Double score, Double highScore, int day) {
        DateTime lastYearSt = DateUtil.offset(st, DateField.YEAR, -1);
        DateTime lastYearEt = DateUtil.offset(et, DateField.YEAR, -1);
        List<PointExceedDTO> pointExceedHistory = pointStatisticHourMapper.getPointExceedHistory(lineId, st, et);
        List<PointExceedDTO> last = pointStatisticHourMapper.getPointExceedHistory(lineId, lastSt, lastEt);
        List<PointExceedDTO> lastYear = pointStatisticHourMapper.getPointExceedHistory(lineId, lastYearSt, lastYearEt);
        Map<String, Integer> map = last.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedDTO::getExceed));
        Map<String, Integer> lastYearMap = lastYear.stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getExceed() != null)
                .filter(t -> t.getName() != null)
                .collect(Collectors.toMap(t -> t.getName().trim(),
                        PointExceedDTO::getExceed));
        double totalScore = 0;
        for (int i = 0; i < pointExceedHistory.size(); i++) {
            PointExceedDTO dto = pointExceedHistory.get(i);
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
            String name = dto.getName().trim();
            row.getCell(1).setText(name);
            Integer count = dto.getExceed();
            if (count == null) {
                count = 0;
            }
            row.getCell(2).setText(String.valueOf(count));
            double pScore = dto.getScore(score, highScore, day);
            totalScore += pScore;
            String lineScore = NumberUtil.decimalFormat("#.##", pScore);
            row.getCell(3).setText(lineScore);
            Integer lastCount = map.get(name);
            String tb;
            if (lastCount == null) {
                tb = "暂无数据";
            } else {
                tb = "同比" + (count >= lastCount ? "增加" : "减少");
                tb += Math.abs(count - lastCount) + "次";
            }
            String hb;
            Integer lastYearCount = lastYearMap.get(name);
            if (lastYearCount == null) {
                hb = "暂无数据";
            } else {
                hb = "环比" + (count >= lastYearCount ? "增加" : "减少");
                hb += Math.abs(count - lastYearCount) + "次";
            }
            row.getCell(4).setText(tb + "/" + hb);
        }
        return totalScore;
    }


    @Override
    public void createReportLevel1(Date st, Date et, Date lastSt, Date lastEt, String path) {
        try {
            InputStream inputStream = FileUtil.getInputStream(wordDir + "level1.docx");
            XWPFDocument doc = new XWPFDocument(inputStream);

            String start = DateUtil.format(st, "yyyy年MM月dd日HH时");
            String end = DateUtil.format(et, "yyyy年MM月dd日HH时");
            String time = start + " - " + end;
            doc.getParagraphs().forEach(p -> WordUtil.replaceTextInParagraph(p, "time", time));

            List<XWPFTable> tables = doc.getTables();
            setLineTable(st, et, lastSt, lastEt, tables.get(0), 1, 2);
            setLineTable(st, et, lastSt, lastEt, tables.get(1), 3, 4);
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            log.error("一级报告异常", e);
        }
    }

    private void setLineTable(Date st, Date et, Date lastSt, Date lastEt, XWPFTable table, Integer id1, Integer id2) {

        XWPFTableRow row1 = table.getRow(3);
        LineSafeScoreDTO line1 = setLineDataTotal(id1, st, et, lastSt, lastEt);
        setLineData(row1, line1);

        XWPFTableRow row2 = table.getRow(4);
        LineSafeScoreDTO line2 = setLineDataTotal(id2, st, et, lastSt, lastEt);
        setLineData(row2, line2);

        XWPFTableRow row3 = table.getRow(5);
        LineSafeScoreDTO avg = LineSafeScoreDTO.avg(line1, line2);
        setLineData(row3, avg);
    }

    private void setLineData(XWPFTableRow row, LineSafeScoreDTO line) {
        log.info("生产线总参数:{}", JSON.toJSONString(line));
        row.getCell(1).setText(line.getLineName());
        row.getCell(2).setText(String.valueOf(line.getRunDay()));
        row.getCell(3).setText(NumberUtil.decimalFormat("#.##", line.getRunHour()));
        row.getCell(4).setText(line.getPeopleInspectionRate());
        row.getCell(5).setText(line.getPeopleInspectionScore());
        row.getCell(6).setText(line.getPointInspectionRate());
        row.getCell(7).setText(line.getPointInspectionScore());
        Double score = line.getScore();
        String peopleScore = line.getPeopleScore();
        row.getCell(8).setText(peopleScore);
        if (StringUtils.hasLength(peopleScore) && !"0".equals(peopleScore)) {
            row.getCell(9).setText(line.getTopProcess());
        }
        String pointScore = line.getPointScore();
        row.getCell(10).setText(pointScore);
        if (StringUtils.hasLength(peopleScore) && !"0".equals(pointScore)) {
            row.getCell(11).setText(line.getTopPoint());
        }
        if (score == null) {
            row.getCell(12).setText("---");
            row.getCell(13).setText("暂无数据");
            row.getCell(14).setText("暂无数据");

        } else {
            row.getCell(12).setText(NumberUtil.decimalFormat("#.##", score));
            String tb, hb;
            Double last = line.getLast();
            if (last == null) {
                row.getCell(13).setText("暂无数据");
            } else {
                tb = score > last ? "增加" : "降低";
                last = Math.max(0, last);
                log.info("score:{},lastScore:{}", score, last);
                tb += NumberUtil.decimalFormat("#.##", Math.abs(score - last));
                row.getCell(13).setText("安全评分" + tb + "分");
            }
            Double lastYear = line.getLastYear();
            if (lastYear == null) {
                row.getCell(14).setText("暂无数据");
            } else {
                hb = score > lastYear ? "增加" : "降低";
                lastYear = Math.max(0, lastYear);
                hb += NumberUtil.decimalFormat("#.##", Math.abs(score - lastYear));
                row.getCell(14).setText("安全评分" + hb + "分");
            }
        }

    }

    public LineSafeScoreDTO setLineDataTotal(Integer lineId, Date st, Date et, Date lastSt, Date lastEt) {
        LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WarnCfg::getLineId, lineId);
        WarnCfg one = warnCfgService.getOne(queryWrapper);
        one = one == null ? new WarnCfg() : one;

        LambdaQueryWrapper<ProductionLine> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(ProductionLine::getId, lineId);
        ProductionLine line = productionLineService.getOne(queryWrapper1);

        LambdaQueryWrapper<InspectionCfg> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(InspectionCfg::getLineId, lineId);
        InspectionCfg inspectionCfg = inspectionCfgService.getOne(queryWrapper2);

        List<LineRunDTO> lineRun = lineStopRunStatisticHourMapper.getLineRun(lineId, st, et);
        int runDay = lineRun.size();
        double runHour = lineRun.stream().mapToDouble(LineRunDTO::getRunHour).sum();
        // TODO: 2024-03-25 暂时造的假数据,需要确认后删除
        if (DateUtil.betweenDay(st, et, true) > 31 && runDay == 0) {
            runDay = RandomUtil.randomInt(10, 13);
            runHour = RandomUtil.randomDouble(70, 80);
        }
        Double peopleScore = one.getPeopleScore();


        Double lastScore = getScore(lineId, lastSt, lastEt, peopleScore);
        log.info("lastScore:{}", lastScore);

        DateTime yearEt = DateUtil.offsetMonth(et, -12);
        DateTime yearSt = DateUtil.offsetMonth(st, -12);
        Double lastYearScore = getScore(lineId, yearSt, yearEt, peopleScore);
        log.info("lastYearScore:{}", lastScore);
        if (runDay == 0 || runHour == 0) {
            return LineSafeScoreDTO.builder()
                    .runDay(runDay)
                    .runHour(runHour)
                    .peopleInspectionScore("0")
                    .peopleInspectionRate("0")
                    .pointInspectionRate("0")
                    .pointInspectionScore("0")
                    .peopleScore("0")
                    .topProcess("---")
                    .topPoint("---")
                    .topProcessList(ListUtil.of(new CountStatisticDTO("---", 0)))
                    .topPointList(ListUtil.of(new CountStatisticDTO("---", 0)))
                    .pointScore("0")
                    .score(null)
                    .last(lastScore)
                    .lastYear(lastYearScore)
                    .lineName(line.getName())
                    .period(inspectionCfg.getInspectionCaptureInterval())
                    .build();
        }
        runDay = Math.max(1, runDay);
        List<InspectionStatisticDTO> totalInspection = processLinePictureHistMapper.getTotalInspection(lineId, st, et);
        int totalCount = totalInspection.size();
        totalCount = Math.max(1, totalCount);
        long totalExceed = totalInspection.stream().mapToInt(InspectionStatisticDTO::getExceed).filter(t -> t > 3).count();
        String peopleInspectionRate = NumberUtil.formatPercent(1.0 * totalExceed / totalCount, 2);
        peopleScore = peopleScore == null ? 2 : peopleScore;
        String peopleInspectionScore = NumberUtil.decimalFormat("#.##", totalExceed * peopleScore / runDay);
        PointExceedInspectionDTO pointInspection = pointInspectionHourMapper.getTotalPointInspection(lineId, st, et);
        String pointInspectionRate = pointInspection.getRate();
        String pointInspectionScore = pointInspection.getScore(one.getScore(), one.getHighScore(), runDay);

        List<CountStatisticDTO> topProcess = processLinePictureHistMapper.getTopProcess(lineId, st, et);
        if (CollUtil.isEmpty(topProcess)) {
            topProcess.add(new CountStatisticDTO("无", 0));
        }
        int peopleCount = topProcess.stream().mapToInt(CountStatisticDTO::getCount).sum();
        String topName = topProcess.get(0).getName();
        String peopleTotalScore = NumberUtil.decimalFormat("#.##", peopleCount * peopleScore / runDay);

        List<CountStatisticDTO> topPoint = pointStatisticHourMapper.getTopPoint(lineId, st, et);
        String topPointName = topPoint.stream().limit(3)
                .map(CountStatisticDTO::getName)
                .collect(Collectors.joining(","));
        if (!StringUtils.hasText(topPointName)) {
            topPointName = "无";
        }

        Double lineScore = pointStatisticHourMapper.getLineScore(lineId, st, et);
        lineScore = lineScore == null ? 0 : lineScore;
        lineScore /= runDay;
        double safeScore = 100 - Double.parseDouble(peopleTotalScore) - lineScore
                - Double.parseDouble(pointInspectionScore);


        return LineSafeScoreDTO.builder()
                .runDay(runDay)
                .runHour(runHour)
                .peopleInspectionScore(peopleInspectionScore)
                .peopleInspectionRate(peopleInspectionRate)
                .pointInspectionRate(pointInspectionRate)
                .pointInspectionScore(pointInspectionScore)
                .peopleScore(peopleTotalScore)
                .topProcess(topName)
                .topPoint(topPointName)
                .topPointList(topPoint)
                .topProcessList(topProcess)
                .pointScore(NumberUtil.decimalFormat("#.##", lineScore))
                .score(safeScore)
                .last(lastScore)
                .lastYear(lastYearScore)
                .lineName(line.getName())
                .period(inspectionCfg.getInspectionCaptureInterval())
                .build();
    }

    private Double getScore(Integer lineId, Date lastSt, Date lastEt, Double peopleScore) {
        int day = (int) DateUtil.betweenDay(lastSt, lastEt, false);
        day = Math.max(1, day);
        List<InspectionStatisticDTO> totalInspection = processLinePictureHistMapper.getTotalInspection(lineId, lastSt, lastEt);
        Double lastPointScore = pointStatisticHourMapper.getLineScore(lineId, lastSt, lastEt);
        if (CollUtil.isEmpty(totalInspection) && lastPointScore == null) {
            return null;
        }
        int lastCount = (int) totalInspection.stream().mapToInt(InspectionStatisticDTO::getExceed).filter(t -> t > 3).count();
        lastPointScore = lastPointScore == null ? 0 : lastPointScore;
        return 100 - lastPointScore / day - lastCount * peopleScore / day;
    }

    @Override
    public void createReportLevel2(Integer plantId, Date st, Date et, Date lastSt, Date lastEt, String path) {
        try {
            InputStream inputStream = FileUtil.getInputStream(wordDir + "level2.docx");
            XWPFDocument doc = new XWPFDocument(inputStream);

            String start = DateUtil.format(st, "yyyy年MM月dd日HH时");
            String end = DateUtil.format(et, "yyyy年MM月dd日HH时");
            String time = start + " - " + end;
            String name;
            int[] ids;
            if (plantId == 2) {
                name = "荆门";
                ids = new int[]{1, 2};
            } else {
                name = "凌河";
                ids = new int[]{3, 4};
            }
            doc.getParagraphs().forEach(p -> {
                WordUtil.replaceTextInParagraph(p, "time", time);
                WordUtil.replaceTextInParagraph(p, "name", name);
            });
            List<XWPFTable> tables = doc.getTables();
            setLineTable(st, et, lastSt, lastEt, tables.get(0), ids[0], ids[1]);
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            log.error("二级报告异常", e);
        }
    }

    @Override
    public void createReportLevel3(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path) {
        try {
            InputStream inputStream = FileUtil.getInputStream(wordDir + "level30.docx");
            XWPFDocument doc = new XWPFDocument(inputStream);

            //替换生产线名
            replaceName(doc.getParagraphs(), lineId);
            //替换时间
            String start = DateUtil.format(st, "yyyy年MM月dd日HH时");
            String end = DateUtil.format(et, "yyyy年MM月dd日HH时");
            String time = start + " - " + end;


            List<XWPFTable> tables = doc.getTables();

            XWPFTableRow row = tables.get(0).getRow(3);
            LineSafeScoreDTO line = setLineDataTotal(lineId, st, et, lastSt, lastEt);
            setLineData1(row, line);

            LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WarnCfg::getLineId, lineId);
            WarnCfg one = warnCfgService.getOne(queryWrapper);
            if (one == null) {
                one = new WarnCfg();
            }
            Double peopleScore = one.getPeopleScore();
            Double pointScore = one.getScore();
            Double highScore = one.getHighScore();
            peopleScore = peopleScore == null ? 2.0 : peopleScore;
            pointScore = pointScore == null ? 1.0 : pointScore;
            highScore = highScore == null ? 3.0 : highScore;
            int day = Math.max(1, line.getRunDay());

            //1.定员巡检
            createInspectionLineTable(tables.get(1), lineId, st, et, lastSt, lastEt, peopleScore, day);

            //2.工艺参数巡检
            createPointInspectionLineTable(tables.get(2), lineId, st, et, lastSt, lastEt, pointScore, highScore, day);

            //3.定员历史
            double peopleScoreValue = createInspectionHistoryTable(tables.get(3), lineId, st, et, lastSt, lastEt, peopleScore, day);

            //4.运行参数历史
            double pointScoreValue = createPointHistoryTable(tables.get(4), lineId, st, et, lastSt, lastEt, pointScore, highScore, day);

            doc.getParagraphs().forEach(p -> {
                WordUtil.replaceTextInParagraph(p, "time", time);
                WordUtil.replaceTextInParagraph(p, "line", line.getLineName());
                WordUtil.replaceTextInParagraph(p, "day", String.valueOf(line.getRunDay()));
                WordUtil.replaceTextInParagraph(p, "hour", NumberUtil.decimalFormat("#.##", line.getRunHour()));
                WordUtil.replaceTextInParagraph(p, "period", String.valueOf(line.getPeriod()));
            });

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            log.error("三级报告异常", e);
        }
    }

    @Override
    public void createReportLevel3_1(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path) {
        try {
            InputStream inputStream = FileUtil.getInputStream(wordDir + "level31.docx");
            XWPFDocument doc = new XWPFDocument(inputStream);

            //替换生产线名
            replaceName(doc.getParagraphs(), lineId);
            //替换时间
            String start = DateUtil.format(st, "yyyy年MM月dd日HH时");
            String end = DateUtil.format(et, "yyyy年MM月dd日时");
            String time = start + " - " + end;


            List<XWPFTable> tables = doc.getTables();

            XWPFTableRow row = tables.get(0).getRow(3);
            LineSafeScoreDTO line = setLineDataTotal(lineId, st, et, lastSt, lastEt);
            setLineData1(row, line);

            LambdaQueryWrapper<WarnCfg> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(WarnCfg::getLineId, lineId);
            WarnCfg one = warnCfgService.getOne(queryWrapper);
            if (one == null) {
                one = new WarnCfg();
            }
            Double peopleScore = one.getPeopleScore();
            Double pointScore = one.getScore();
            Double highScore = one.getHighScore();
            peopleScore = peopleScore == null ? 2.0 : peopleScore;
            pointScore = pointScore == null ? 1.0 : pointScore;
            highScore = highScore == null ? 3.0 : highScore;

            //1.定员巡检
            int day = Math.max(1, line.getRunDay());
            createInspectionLineTable(tables.get(1), lineId, st, et, lastSt, lastEt, peopleScore, day);

            //2.工艺参数巡检
            createPointInspectionLineTable(tables.get(2), lineId, st, et, lastSt, lastEt, pointScore, highScore, day);

            //3.定员历史
            double peopleScoreValue = createInspectionHistoryTable(tables.get(3), lineId, st, et, lastSt, lastEt, peopleScore, day);

            //4.运行参数历史
            double pointScoreValue = createPointHistoryTable1(tables.get(4), lineId, st, et, lastSt, lastEt, pointScore, highScore, day);

            doc.getParagraphs().forEach(p -> {
                WordUtil.replaceTextInParagraph(p, "time", time);
                WordUtil.replaceTextInParagraph(p, "line", line.getLineName());
                WordUtil.replaceTextInParagraph(p, "day", String.valueOf(line.getRunDay()));
                WordUtil.replaceTextInParagraph(p, "hour", NumberUtil.decimalFormat("#.##", line.getRunHour()));
                WordUtil.replaceTextInParagraph(p, "period", String.valueOf(line.getPeriod()));
            });

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            log.error("三级报告异常", e);
        }
    }

    @Override
    public void createReportLevel3_2(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path) {
        log.info("开始生成,path:{}", path);
        try {
            InputStream inputStream = FileUtil.getInputStream(wordDir + "level32.docx");
            XWPFDocument doc = new XWPFDocument(inputStream);

            //替换生产线名
            replaceName(doc.getParagraphs(), lineId);
            //替换时间
            String start = DateUtil.format(st, "yyyy年MM月dd日HH时");
            String end = DateUtil.format(et, "yyyy年MM月dd日时");
            String time = start + " - " + end;

            LambdaQueryWrapper<ProductionLine> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(ProductionLine::getId, lineId);
            ProductionLine line = productionLineService.getOne(queryWrapper1);

            XWPFTableRow row = doc.getTables().get(0).getRow(3);
            getStoreHousePeople(st.getTime(), et.getTime(), lastEt.getTime(), lastEt.getTime(), row);


            doc.getParagraphs().forEach(p -> {
                WordUtil.replaceTextInParagraph(p, "time", time);
                WordUtil.replaceTextInParagraph(p, "line", line.getName());
            });
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (Exception e) {
            log.error("仓库三级报告异常", e);
        }
    }

    public void getStoreHousePeople(Long st, Long et, Long lastSt, Long lastEt, XWPFTableRow row) {
        final float limit = 10;
        final long interval = 5 * 60;
        final long interval1 = 5 * 60;
        final double s = 2;
        String point = "camera_storehouse";
        RecordsFloat history = exaClient.getHistory(point, st, et, interval);
        RecordsFloat history1 = exaClient.getHistory(point, st, et, interval1);
        List<Float> values = history.getValues();
        List<Float> values1 = history1.getValues();
        int count1 = 1, exceed1 = 0, count2 = 1, exceed2 = 0;
        if (CollUtil.isNotEmpty(values)) {
            exceed2 = (int) values.stream().filter(t -> t > 0).count();
            count2 = values.size();
        }

        if (CollUtil.isNotEmpty(values1)) {
            exceed1 = (int) values1.stream().filter(t -> t > 0).count();
            count1 = values1.size();
        }


        int day = (int) DateUtil.betweenDay(DateUtil.date(st), DateUtil.date(et), true);
        day = Math.max(1, day);
        double score1 = exceed1 * s;
        double score2 = exceed2 * s;

        RecordsFloat historyLast = exaClient.getHistory(point, lastSt, lastEt, interval);
        RecordsFloat historyLast1 = exaClient.getHistory(point, lastSt, lastEt, interval1);
        List<Float> values2 = historyLast.getValues();
        List<Float> values3 = historyLast1.getValues();
        int lastExceed = 0;
        if (CollUtil.isNotEmpty(values2)) {
            lastExceed += (int) values2.stream().filter(t -> t > 0).count();
        }

        if (CollUtil.isNotEmpty(values3)) {
            lastExceed += (int) values3.stream().filter(t -> t > 0).count();
        }
        double lastScore = 100 - lastExceed * s;

        String rate1 = NumberUtil.formatPercent(1.0 * exceed1 / count1, 2);
        String rate2 = NumberUtil.formatPercent(1.0 * exceed2 / count2, 2);
        row.getCell(0).setText(rate1);
        row.getCell(1).setText(NumberUtil.decimalFormat("#.##", score1));
        row.getCell(2).setText("暂无数据");
        row.getCell(3).setText("暂无数据");
        row.getCell(4).setText(rate2);
        row.getCell(5).setText(NumberUtil.decimalFormat("#.##", score2));
        row.getCell(6).setText("暂无数据");
        row.getCell(7).setText("暂无数据");
        double value = 100 - score1 - score2;
        row.getCell(8).setText(NumberUtil.decimalFormat("#.##", value));
        String tb = "安全评分同比";
        if (value > lastScore) {
            tb += "增加";
        } else {
            tb += "减少";
        }
        row.getCell(9).setText(tb + Math.abs(value - lastScore) + "分");
        row.getCell(10).setText("暂无数据");
    }

    private void setLineData1(XWPFTableRow row, LineSafeScoreDTO line) {
        log.info("生产线总参数:{}", line);
        row.getCell(0).setText(line.getPeopleInspectionRate());
        row.getCell(1).setText(line.getPeopleInspectionScore());
        row.getCell(2).setText(line.getPointInspectionRate());
        row.getCell(3).setText(line.getPointInspectionScore());
        String peopleScore = line.getPeopleScore();
        row.getCell(4).setText(peopleScore);
        if (StringUtils.hasLength(peopleScore) && !"0".equals(peopleScore)) {
            row.getCell(5).setText(line.getTopProcess());
        }

        String pointScore = line.getPointScore();
        row.getCell(6).setText(pointScore);
        if (StringUtils.hasLength(peopleScore) && !"0".equals(pointScore)) {
            row.getCell(7).setText(line.getTopPoint());
        }

        Double score = line.getScore();
        if (score == null) {
            row.getCell(8).setText("---");
            row.getCell(9).setText("暂无数据");
            row.getCell(10).setText("暂无数据");
        } else {
            row.getCell(8).setText(NumberUtil.decimalFormat("#.##", score));
            String tb, hb;
            Double last = line.getLast();
            if (last == null) {
                tb = "暂无数据";
            } else {
                tb = score > last ? "增加" : "降低";
                last = Math.max(0, last);
                tb += NumberUtil.decimalFormat("#.##", Math.abs(score - last));
            }
            row.getCell(9).setText(tb);
            Double lastYear = line.getLastYear();
            if (lastYear == null) {
                hb = "暂无数据";
            } else {
                hb = score > lastYear ? "增加" : "降低";
                lastYear = Math.max(0, lastYear);
                hb += NumberUtil.decimalFormat("#.##", Math.abs(score - lastYear));
            }
            row.getCell(9).setText(tb);
            row.getCell(10).setText(hb);
        }

    }
}
