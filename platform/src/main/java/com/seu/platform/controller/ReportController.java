package com.seu.platform.controller;

import cn.hutool.core.io.FileUtil;
import com.seu.platform.model.entity.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
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


    @GetMapping("/month")
    public Result<String> getMonthReport(@RequestHeader("Authorization") String token, Integer lineId) {
        String prefix = "http://localhost:8088/api/report/download/";
        return Result.success(prefix + "inspection.docx");
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/download/{fileName}")
    public void downloadExport(HttpServletResponse response, @PathVariable String fileName) {
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        Resource resource = resourceLoader.getResource("classpath:word/" + fileName);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] bytes = FileUtil.readBytes(resource.getFile().getAbsolutePath());
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("获取word文档异常:{}", fileName, e);
        }
    }
}
