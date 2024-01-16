package com.seu.platform.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 13:34
 */
@UtilityClass
public class WordUtil {

    public static String extractHTMLFromWord(XWPFDocument doc) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        return out.toString();
    }
}
