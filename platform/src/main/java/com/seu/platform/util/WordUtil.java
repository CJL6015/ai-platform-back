package com.seu.platform.util;

import lombok.experimental.UtilityClass;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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

    public static void replaceTextInParagraph(XWPFParagraph paragraph, String oldText, String newText) {
        List<XWPFRun> runs = paragraph.getRuns();
        runs.forEach(run -> {
            String text = run.getText(run.getTextPosition());
            if (text != null && text.contains(oldText)) {
                text = text.replace(oldText, newText);
                run.setText(text, 0);
            }
        });
    }

    private static void mergeAdjacentRuns(List<XWPFRun> runs) {
        if (runs.size() > 1) {
            for (int i = runs.size() - 1; i > 0; i--) {
                XWPFRun currentRun = runs.get(i);
                XWPFRun previousRun = runs.get(i - 1);

                // 检查相邻运行的样式是否相同，这里以字体大小为例
                if (currentRun.getFontSize() == previousRun.getFontSize()) {
                    // 合并相邻运行的文本
                    String mergedText = previousRun.getText(0) + currentRun.getText(0);

                    // 设置新的文本到前一个运行
                    previousRun.setText(mergedText, 0);

                    // 移除当前运行
                    runs.remove(i);
                }
            }
        }
    }

}
