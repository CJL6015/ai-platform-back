package com.seu.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
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
    private final ProcessLinePictureHistService processLinePictureHistService;


    @Scheduled(fixedDelay = 10000)
    public void doTask() {
        List<LineInspection> last = processLinePictureHistService.getLast();
        if (CollUtil.isEmpty(last)) {
            log.info("本次巡检暂无配置");
            return;
        }
        DateTime now = DateUtil.beginOfHour(new Date());
        Integer lineId = null;
        DateTime inspectionTime = null;
        for (LineInspection inspection : last) {
            Date time = inspection.getTime();
            String cameraIp = inspection.getCameraIp().trim();
            Integer interval = inspection.getInterval();
            if (Objects.isNull(time)) {
                time = processLinePictureHistService.getFirstTime(cameraIp);
                time = DateUtil.offsetHour(time, -interval);
            }
            DateTime lasTime = DateUtil.beginOfHour(time);
            long diff = DateUtil.between(lasTime, now, DateUnit.HOUR);
            if (diff > interval) {
                DateTime st;
                DateTime et;
                if (Objects.equals(inspection.getLineId(), lineId) && Objects.nonNull(inspectionTime)) {
                    st = inspectionTime;
                    et = DateUtil.offsetMinute(st, 1);
                } else {
                    st = DateUtil.offsetHour(lasTime, interval);
                    et = DateUtil.offsetMinute(st, 1);
                    if (0 == inspection.getMode()) {
                        Random random = new Random();
                        int minutes = random.nextInt(60);
                        et = DateUtil.offsetMinute(st, minutes);
                    }
                    inspectionTime = st;
                    lineId = inspection.getLineId();
                }
                Date nextTime = processLinePictureHistService.getNextTime(cameraIp, st);
                if (Objects.nonNull(nextTime) && nextTime.after(et)) {
                    et = DateTime.of(nextTime);
                }
                Boolean result = processLinePictureHistService.setInspectionMinute(cameraIp, st, et);
                log.info("{}巡检时间为{}-{},结果:{}", cameraIp, st, et, result);
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void doTask1() {
        List<LineInspection> last = processLinePictureHistService.getLast1();
        if (CollUtil.isEmpty(last)) {
            log.info("本次巡检暂无配置");
            return;
        }
        DateTime now = DateUtil.beginOfHour(new Date());
        Integer lineId = null;
        DateTime inspectionTime = null;
        for (LineInspection inspection : last) {
            Date time = inspection.getTime();
            String cameraIp = inspection.getCameraIp().trim();
            int interval = 10;
            if (Objects.isNull(time)) {
                time = processLinePictureHistService.getFirstTime(cameraIp);
                time = DateUtil.offsetMinute(time, -interval);
            }
            DateTime lasTime = DateUtil.beginOfMinute(time);
            long diff = DateUtil.between(lasTime, now, DateUnit.MINUTE);
            if (diff > interval) {
                DateTime st;
                DateTime et;
                if (Objects.equals(inspection.getLineId(), lineId) && Objects.nonNull(inspectionTime)) {
                    st = inspectionTime;
                    et = DateUtil.offsetMinute(st, 1);
                } else {
                    st = DateUtil.offsetMinute(lasTime, interval);
                    et = DateUtil.offsetMinute(st, 1);
                    if (0 == inspection.getMode()) {
                        Random random = new Random();
                        int minutes = random.nextInt(60);
                        et = DateUtil.offsetMinute(st, minutes);
                    }
                    inspectionTime = st;
                    lineId = inspection.getLineId();
                }
                Date nextTime = processLinePictureHistService.getNextTime(cameraIp, st);
                if (Objects.nonNull(nextTime) && nextTime.after(et)) {
                    et = DateTime.of(nextTime);
                }
                Boolean result = processLinePictureHistService.setInspectionMinute1(cameraIp, st, et);
                log.info("{}巡检时间为{}-{},结果:{}", cameraIp, st, et, result);
            }
        }
    }
}
