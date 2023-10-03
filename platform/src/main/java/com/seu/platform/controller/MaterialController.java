package com.seu.platform.controller;

import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-02 20:38
 */
@RestController
@RequestMapping("/api/static")
public class MaterialController {
    @Value("${static.root}")
    private String root;

    @GetMapping(value = "/images/{path}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(@PathVariable String path) {
        String filePath = root + path;
        return FileUtil.readBytes(filePath);
    }
}
