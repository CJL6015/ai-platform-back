package com.seu.platform.task;

import com.seu.platform.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

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


    @Scheduled(fixedRate = 100)
    public void generatePlantReport() {
        try {
//            Resource resource = resourceLoader.getResource("classpath:word/plant.docx");
//            InputStream inputStream = resource.getInputStream();
//            XWPFDocument doc = new XWPFDocument(inputStream);
//            XWPFTable table = doc.getTables().get(0);
            reportService.createLineReport(1);
            System.out.println(1);

        } catch (Exception e) {
            log.error("生成word异常", e);
        }
    }

    public void generateInspectionReport() {
        try {
            Resource resource = resourceLoader.getResource("classpath:word/inspection.docx");
            InputStream inputStream = resource.getInputStream();
            XWPFDocument doc = new XWPFDocument(inputStream);
            XWPFTable table = doc.getTables().get(0);
            System.out.println(1);

        } catch (IOException e) {
            log.error("生成word异常", e);
        }
    }
}
