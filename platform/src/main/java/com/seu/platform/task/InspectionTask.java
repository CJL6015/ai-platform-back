package com.seu.platform.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.exa.ExaClient;
import com.seu.platform.exa.model.RecordsFloat;
import com.seu.platform.model.entity.LineInspection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionTask {
    private final ProcessLinePictureHistService processLinePictureHistService;

    private final ExaClient exaClient;

    private Map<Integer, String> run = new HashMap<>(6);

    @PostConstruct
    public void initMap() {
        run.put(1, "KLRH3_STOP_RUN");
        run.put(2, "KLPH_STOP_RUN");
        run.put(3, "KLLGA_STOP_RUN");
        run.put(4, "KLLGB_STOP_RUN");
    }

    @Scheduled(fixedDelay = 60000)
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
                        int between = (int) DateUtil.between(st, DateUtil.date(), DateUnit.MINUTE);
                        int minutes = random.nextInt(Math.min(50, between - 5));
                        st = DateUtil.offsetMinute(st, minutes);
                        et = DateUtil.offsetMinute(st, 1);
                    }
                    inspectionTime = st;
                    lineId = inspection.getLineId();
                }
                Date nextTime = processLinePictureHistService.getNextTime(cameraIp, st);
                if (Objects.nonNull(nextTime) && nextTime.after(et)) {
                    et = DateTime.of(nextTime);
                }
                RecordsFloat history = exaClient.getHistory(run.get(lineId), st.getTime(), et.getTime() + 300);
                List<Float> values = history.getValues();
                Float v;
                if (CollUtil.isNotEmpty(values)) {
                    v = values.get(0);
                } else {
                    v = 1F;
                }
                Boolean result;
                int code;
                if (v != null && v > 0) {
                    code = 2;
                } else {
                    code = 1;
                }
                result = processLinePictureHistService.setInspectionMinute(cameraIp, st, et, code);
                log.info("{}巡检时间为{}-{},结果:{},生产线运行状态:{}", cameraIp, st, et, result, code);
            }
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void doTask1() {
        log.info("开始普通巡检");
        List<LineInspection> last = processLinePictureHistService.getLast1();
        if (CollUtil.isEmpty(last)) {
            log.info("本次巡检暂无配置");
            return;
        }
        try {
            DateTime now = DateUtil.beginOfHour(new Date());
            for (LineInspection inspection : last) {
                Date time = inspection.getTime();
                String cameraIp = inspection.getCameraIp().trim();
                int interval = 20;
                if (Objects.isNull(time)) {
                    time = processLinePictureHistService.getFirstTime(cameraIp);
                    time = DateUtil.offsetMinute(time, -interval);
                }
                DateTime lasTime = DateUtil.beginOfMinute(time);
                long diff = DateUtil.between(lasTime, now, DateUnit.MINUTE);
                if (diff > interval) {
                    Date st;
                    DateTime et;
                    st = DateUtil.offsetMinute(lasTime, interval);
                    st = processLinePictureHistService.getNextTime(cameraIp, st);
                    et = DateUtil.offsetMinute(st, 1);
                    Boolean result = processLinePictureHistService.setInspectionMinute1(cameraIp, st, et);
                    log.info("{}普通巡检时间为{}-{},结果:{}", cameraIp, st, et, result);
                }
            }
        } catch (Exception e) {
            log.error("普通巡检异常", e);
        }
    }
}
