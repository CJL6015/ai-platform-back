package com.seu.platform.service;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 13:34
 */
public interface ReportService {


    void createReportLevel1(Date st, Date et, Date lastSt, Date lastEt, String path);

    void createReportLevel2(Integer plantId, Date st, Date et, Date lastSt, Date lastEt, String path);

    void createReportLevel3(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path);

    void createReportLevel3_1(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path);
    void createReportLevel3_2(Integer lineId, Date st, Date et, Date lastSt, Date lastEt, String path);

}
