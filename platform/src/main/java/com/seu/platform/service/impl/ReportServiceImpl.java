package com.seu.platform.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.CameraCfgMapper;
import com.seu.platform.dao.mapper.PointInspectionHourMapper;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.dto.CountStatisticDTO;
import com.seu.platform.model.dto.PointExceedInspectionDTO;
import com.seu.platform.model.dto.PointReportDTO;
import com.seu.platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    public void createLineReport(Integer lineId) {
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

            DateTime date = DateUtil.date();
            DateTime et = DateUtil.beginOfMonth(date);
            DateTime st = DateUtil.offsetMonth(et, -1);
            XWPFTable peopleTable = doc.getTables().get(0);
            setPeopleTable(lineId, st, date, peopleTable, peopleScore);
            setPointTable(lineId, st, date, doc, highScore, pointScore);
            String path = "C:\\work\\demo\\report.docx";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            doc.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setPointTable(Integer lineId, DateTime st, DateTime et, XWPFDocument doc, Double highScore, Double pointScore) {
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

    private void setPeopleTable(Integer lineId, DateTime st, DateTime et, XWPFTable peopleTable, Double peopleScore) {
        List<CountStatisticDTO> topProcess = processLinePictureHistMapper.getTopProcess(1, st, et);

        int totalExceedCount = 0;
        for (int i = 0; i < topProcess.size(); i++) {
            CountStatisticDTO countStatisticDTO = topProcess.get(i);
            XWPFTableRow row = peopleTable.createRow();
            row.getCell(0).setText(String.valueOf(i + 1));
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
            String rate = NumberUtil.formatPercent(100.0 * exceed / count, 2);

            row.getCell(0).setText(String.valueOf(i + 1));
            row.getCell(1).setText(pointsMap.get(id1[i]).getDescription().trim());
            row.getCell(2).setText(String.valueOf(count));
            row.getCell(3).setText(String.valueOf(exceed));
            row.getCell(4).setText(String.valueOf(rate));
        }

        List<PointReportDTO> report2 = pointInspectionHourMapper.getPointReport(id2);

    }
}
