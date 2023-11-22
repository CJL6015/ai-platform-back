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
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionTask {
    private final ProcessLinePictureHist1Service processLinePictureHist1Service;

    private final InspectionCfgService inspectionCfgService;


    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = "0 0/5 * * * *")
    public void doTask() {
        List<LineInspection> last = processLinePictureHist1Service.getLast();
        if (CollUtil.isEmpty(last)) {
            log.info("本次巡检暂无配置");
            return;
        }
        DateTime now = DateUtil.beginOfHour(new Date());
        for (LineInspection inspection : last) {
            Date time = inspection.getTime();
            String cameraIp = inspection.getCameraIp().trim();
            Integer interval = inspection.getInterval();
            if (Objects.isNull(time)) {
                time = processLinePictureHist1Service.getFirstTime(cameraIp);
                time = DateUtil.offsetHour(time, -interval);
            }
            DateTime lasTime = DateUtil.beginOfHour(time);
            long diff = DateUtil.between(lasTime, now, DateUnit.HOUR);
            if (diff > interval) {
                DateTime st = DateUtil.offsetHour(lasTime, interval);
                if (0 == inspection.getMode()) {
                    Random random = new Random();
                    int minutes = random.nextInt(60);
                    st = DateUtil.offsetMinute(st, minutes);
                }
                DateTime et = DateUtil.offsetMinute(st, 1);
                Boolean result = processLinePictureHist1Service.setInspectionMinute(cameraIp, st, et);
                log.info("{}巡检时间为{},结果:{}", cameraIp, st, result);
            }
        }
    }
}