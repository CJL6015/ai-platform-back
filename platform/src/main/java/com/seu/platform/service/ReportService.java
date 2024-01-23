package com.seu.platform.service;

import java.util.Date;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-01-06 13:34
 */
public interface ReportService {

    void createLineReport(Integer lineId, Date st, Date et, String path);

    void createPlantReport(Date st, Date et, String path);

    void createInspectionReport(Date st, Date et, String path);

    void createReport1(Date st, Date et, String path);


    void createReport2(Date st, Date et, String path);

    void createReport3(Integer lineId,Date st,Date et,String path);

}
