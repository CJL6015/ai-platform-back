package com.seu.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.service.InspectionCfgService;
import com.seu.platform.dao.service.ProcessLinePictureHist1Service;
import com.seu.platform.model.entity.LineInspection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionTask {
    private final ProcessLinePictureHist1Service processLinePictureHist1Service;

    private final InspectionCfgService inspectionCfgService;


    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void doTask() {
        List<LineInspection> last = processLinePictureHist1Service.getLast();
        if (CollUtil.isEmpty(last)) {
            log.info("本次巡检暂无配置");
            return;
        }
        DateTime now = DateUtil.beginOfHour(new Date());
        for (LineInspection inspection : last) {
            Date time = inspection.getTime();
            if (Objects.isNull(time)) {

            }
            DateTime lasTime = DateUtil.beginOfHour(time);
            long diff = DateUtil.between(lasTime, now, DateUnit.HOUR);
            Integer interval = inspection.getInterval();
            if (diff >= interval) {

            }
        }
    }
}
