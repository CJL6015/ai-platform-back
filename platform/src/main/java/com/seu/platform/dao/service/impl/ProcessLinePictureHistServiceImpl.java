package com.seu.platform.dao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.CameraCfg;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.entity.WarnCfg;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.CameraCfgService;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.dao.service.WarnCfgService;
import com.seu.platform.model.dto.DetectionTrendDTO;
import com.seu.platform.model.dto.HourTrendDTO;
import com.seu.platform.model.dto.TrendDTO;
import com.seu.platform.model.entity.LineInspection;
import com.seu.platform.model.vo.*;
import com.seu.platform.util.MathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist】的数据库操作Service实现
 * @createDate 2023-10-28 10:48:15
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessLinePictureHistServiceImpl extends ServiceImpl<ProcessLinePictureHistMapper, ProcessLinePictureHist>
        implements ProcessLinePictureHistService {

    private static Map<String, List<Date>> cache = new HashMap<>(16);
    private final CameraCfgService cameraCfgService;
    private final WarnCfgService warnCfgService;
    @Value("${static.detection-prefix}")
    private String picturePrefix;

    @Value("${static.detection-prefix1}")
    private String picturePrefix1;

    private static List<Double> getPredictions(List<Integer> y, double[] parameters) {
        List<Double> predictions = new ArrayList<>();
        for (int i = 0; i < y.size(); i++) {
            double prediction = parameters[1] * i + parameters[0];
            predictions.add(prediction);
        }
        return predictions;
    }

    @Override
    @Cacheable("getPendingChecks")
    public List<ProcessLinePictureHist> getPendingChecks(Integer count, Set<Long> ids) {
        return getBaseMapper().getPendingChecks(count, ids);
    }

    @Override
    @Cacheable("getDetectionResult")
    public List<DetectionResultVO> getDetectionResult(Integer lineId, Date time) {
        if (Objects.isNull(time)) {
            time = getBaseMapper().lastTime(lineId);
        }
        LambdaQueryWrapper<WarnCfg> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(WarnCfg::getLineId, lineId);
        WarnCfg one = warnCfgService.getOne(queryWrapper1);
        Integer limit = one.getFillingProcessLimit();
        limit = limit == null ? 1 : limit;
        LambdaQueryWrapper<CameraCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CameraCfg::getLineId, lineId);
        List<CameraCfg> list = cameraCfgService.list(queryWrapper);
        List<String> ips = list.stream().map(t -> t.getCameraIp().trim()).collect(Collectors.toList());
        List<DetectionResultVO> detectionResult = new ArrayList<>();
        if (time != null) {
            time = DateUtil.beginOfHour(time);
            detectionResult = getBaseMapper().getDetectionResult(ips, time);
        }
        List<DetectionResultVO> res = new ArrayList<>();
        String prefix = (lineId == 3 || lineId == 4 || lineId == 5) ? picturePrefix1 : picturePrefix;
        for (CameraCfg cameraCfg : list) {
            DetectionResultVO vo = new DetectionResultVO();
            for (DetectionResultVO detectionResultVO : detectionResult) {
                if (cameraCfg.getCameraIp().equals(detectionResultVO.getCameraId())) {
                    BeanUtil.copyProperties(detectionResultVO, vo);
                    vo.setDetectionPicturePath(prefix + detectionResultVO.getDetectionPicturePath());
                }
            }
            if (vo.getPeopleCount() == null) {
                vo.setPeopleCount(0);
            }
            vo.setLimit(limit);
            vo.setExceeded(Math.max(0, vo.getPeopleCount() - limit));
            vo.setDescription(cameraCfg.getCameraDescription().trim());
            vo.setCameraId(cameraCfg.getCameraIp().trim());
            res.add(vo);
        }
        return res;
    }


    @Override
    public List<DetectionResultVO> getSnapshotResult(Integer lineId) {
        LambdaQueryWrapper<WarnCfg> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(WarnCfg::getLineId, lineId);
        WarnCfg one = warnCfgService.getOne(queryWrapper1);
        Integer limit = one.getFillingProcessLimit();
        limit = limit == null ? 1 : limit;
        LambdaQueryWrapper<CameraCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CameraCfg::getLineId, lineId);
        List<CameraCfg> list = cameraCfgService.list(queryWrapper);
        List<String> ips = list.stream().map(t -> t.getCameraIp().trim()).collect(Collectors.toList());
        List<DetectionResultVO> detectionResult = getBaseMapper().getSnapshotResult(ips);
        List<DetectionResultVO> res = new ArrayList<>();
        String prefix = (lineId == 3 || lineId == 4 || lineId == 5) ? picturePrefix1 : picturePrefix;
        for (CameraCfg cameraCfg : list) {
            DetectionResultVO vo = new DetectionResultVO();
            for (DetectionResultVO detectionResultVO : detectionResult) {
                if (cameraCfg.getCameraIp().equals(detectionResultVO.getCameraId())) {
                    BeanUtil.copyProperties(detectionResultVO, vo);
                    vo.setDetectionPicturePath(prefix + detectionResultVO.getDetectionPicturePath());
                }
            }
            if (vo.getPeopleCount() == null) {
                vo.setPeopleCount(0);
            }
            vo.setLimit(limit);
            vo.setExceeded(Math.max(0, vo.getPeopleCount() - limit));
            vo.setDescription(cameraCfg.getCameraDescription().trim());
            vo.setCameraId(cameraCfg.getCameraIp().trim());
            res.add(vo);
        }
        return res;
    }

    @Override
    public List<String> getTimes(Integer lineId, Date time) {
        log.info("查询时间:{}", time);
        Date et = DateUtil.endOfDay(time);
        DateTime st = DateUtil.beginOfDay(time);
        List<Date> detectionTime = getBaseMapper().getDetectionTime(lineId, st, et);
        log.info("巡检时间:{}", detectionTime);
        return detectionTime.stream()
                .map(t -> DateUtil.format(t, "yyyy-MM-dd HH:mm:ss"))
                .collect(Collectors.toList());
    }


    @Override
    public TrendVO<String, Integer> getTrendMonth(Integer lineId, TimeRange timeRange) {
        List<TrendDTO> monthTrend = getBaseMapper().getMonthTrend(lineId, timeRange.getSt(), timeRange.getEt());
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : monthTrend) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public TrendVO<String, Integer> getTrendDaily(Integer lineId, TimeRange timeRange) {
        List<TrendDTO> dailyTrend = getBaseMapper().getDailyTrend(lineId, timeRange.getSt(), timeRange.getEt());
        List<String> times = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日HH时");
        for (TrendDTO dto : dailyTrend) {
            times.add(dateFormat.format(dto.getTime()));
            counts.add(dto.getCount());
        }
        double[] doubles = MathUtil.fitting(counts, 1);
        List<Double> fitValues = getPredictions(counts, doubles);
        return new TrendVO<>(times, counts, fitValues, doubles);
    }

    @Override
    public DetectionTrendVO getPeopleTrend(Integer lineId, Date st, Date et) {
        List<DetectionTrendDTO> peopleTrend = getBaseMapper().getPeopleTrend(lineId, st, et);
        List<String> names = new ArrayList<>();
        List<List<Object[]>> data = new ArrayList<>();
        List<Object[]> value = new ArrayList<>();
        List<Integer> peopleMax = new ArrayList<>();
        List<Integer> countMax = new ArrayList<>();
        data.add(value);
        List<Integer> counts = new ArrayList<>();
        List<String> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (CollUtil.isNotEmpty(peopleTrend)) {
            String name = peopleTrend.get(0).getName().trim();
            int count = 0;
            int max = 0;
            int maxPeople = 0;
            for (DetectionTrendDTO dto : peopleTrend) {
                String processName = dto.getName().trim();
                if (!names.contains(processName)) {
                    names.add(processName);
                }
                if (!name.equals(processName)) {
                    value = new ArrayList<>();
                    data.add(value);
                    name = processName;
                    counts.add(count);
                    count = 0;
                    countMax.add(max);
                    max = 0;
                    peopleMax.add(maxPeople);
                    maxPeople = 0;
                }
                Integer dtoCount = dto.getCount();
                max = Math.max(max, dtoCount);
                Integer maxCount = dto.getMaxCount();
                maxPeople = Math.max(maxCount, maxPeople);
                count += dtoCount;
                value.add(new Object[]{dateFormat.format(dto.getTime()), dtoCount});
            }
            counts.add(count);
            countMax.add(max);
            peopleMax.add(maxPeople);
            for (List<Object[]> list : data) {
                List<Integer> nums = new ArrayList<>();
                for (Object[] objects : list) {
                    nums.add((int) objects[1]);
                }
                double a = MathUtil.fitting(nums, 1)[1];
                trend.add(a > 0 ? "上升趋势" : "下降趋势");
            }
        }

        return DetectionTrendVO.builder()
                .values(data)
                .names(names)
                .peopleMax(peopleMax)
                .countMax(countMax)
                .counts(counts)
                .trend(trend)
                .build();
    }

    @Override
    public BenchmarkTrendVO getBenchmarkTrend(Integer lineId, Date st, Date et) {
        List<TrendDTO> totalTrend = getBaseMapper().getTotalTrend(lineId, st, et);
        List<Object[]> trend = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (TrendDTO dto : totalTrend) {
            trend.add(new Object[]{dateFormat.format(dto.getTime()), dto.getCount()});
        }

        List<HourTrendDTO> timeOverrun = getBaseMapper().getTimeOverrun(lineId, st, et);
        int[] hours = new int[24];
        for (HourTrendDTO dto : timeOverrun) {
            Integer time = dto.getTime();
            hours[time] = dto.getCount();
        }
        List<Integer> nums = totalTrend.stream().map(TrendDTO::getCount).collect(Collectors.toList());
        double a = MathUtil.fitting(nums, 1)[1];
        return BenchmarkTrendVO.builder()
                .trend(trend)
                .hours(hours)
                .summary(a > 0 ? "上升趋势" : "下降趋势")
                .build();
    }

    @Override
    public List<LineInspection> getLast() {
        return getBaseMapper().getLast();
    }

    @Override
    public List<LineInspection> getLast1() {
        return getBaseMapper().getLast1();
    }

    @Override
    public Integer exceedCount(Integer lineId, Date st, Date et) {
        return getBaseMapper().exceedCount(lineId, st, et);
    }

    @Override
    public Boolean setInspectionMinute(String cameraIp, Date st, Date et, Integer code) {
        return getBaseMapper().setInspectionMinute(cameraIp, st, et, code);
    }

    @Override
    public Boolean setInspectionMinute1(String cameraIp, Date st, Date et) {
        return getBaseMapper().setInspectionMinute1(cameraIp, st, et);
    }

    @Override
    public Date getFirstTime(String cameraIp) {
        return getBaseMapper().getFirstTime(cameraIp);
    }

    @Override
    public Date getNextTime(String cameraIp, Date st) {
        return getBaseMapper().getNextTime(cameraIp, st);
    }
}




