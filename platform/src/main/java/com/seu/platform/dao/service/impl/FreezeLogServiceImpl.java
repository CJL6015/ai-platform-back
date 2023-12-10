package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.FreezeLog;
import com.seu.platform.dao.mapper.FreezeLogMapper;
import com.seu.platform.dao.service.FreezeLogService;
import com.seu.platform.model.dto.InspectionHistoryDTO;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import com.seu.platform.model.vo.InspectionHistoryVO;
import com.seu.platform.model.vo.TimeValueChartVO;
import com.seu.platform.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈小黑
 * @description 针对表【freeze_log(冻结日志)】的数据库操作Service实现
 * @createDate 2023-09-16 16:16:52
 */
@Service
@RequiredArgsConstructor
public class FreezeLogServiceImpl extends ServiceImpl<FreezeLogMapper, FreezeLog>
        implements FreezeLogService {
    private static final int STEP = 60 * 60 * 1000;

    private static final double HOUR = 60 * 60 * 1000.0;


    @Override
    public InspectionHistoryDataVO getInspectionHistoryFreeze(Integer lineId, Date st, Date et) {
        List<InspectionHistoryDTO> histories = getBaseMapper().getHistoryFreeze(lineId, st, et);
        List<Integer> values = new ArrayList<>();
        List<String> timestamps = new ArrayList<>();
        values.add(null);
        long t1 = st.getTime();
        List<InspectionHistoryVO> tableData = new ArrayList<>();
        int freezeCount = 0;
        int unfreezeCount = 0;
        for (InspectionHistoryDTO history : histories) {
            long start = history.getSt().getTime();
            fillValue(timestamps, values, t1, start, null);
            long end = history.getEt().getTime();
            fillValue(timestamps, values, start, end, 1);
            t1 = end;
            Integer exceededNum = history.getExceededNum();
            if (Objects.nonNull(exceededNum)) {
                freezeCount += exceededNum;
            } else {
                history.setExceededNum(0);
            }
            if (StringUtils.hasText(history.getImageUrl())) {
                String[] imgs = history.getImageUrl().split(",");
                for (int i = 0; i < imgs.length; i++) {
                    InspectionHistoryVO vo = BeanUtil.convertBean(history, InspectionHistoryVO.class);
                    vo.setFreezeTime((end - start) / HOUR);
                    vo.setImageUrl(new String[]{imgs[i]});
                    vo.setExceededPeople(i + 1);
                    tableData.add(vo);
                }
            }


        }
        fillValue(timestamps, values, t1, et.getTime(), null);
        TimeValueChartVO vo = new TimeValueChartVO(timestamps, values);
        return new InspectionHistoryDataVO(vo, tableData, unfreezeCount, freezeCount);
    }

    private void fillValue(List<String> timestamps, List<Integer> values, long st, long et, Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (long t = st; t <= et; t += STEP) {
            timestamps.add(dateFormat.format(t));
            values.add(value);
        }
    }
}




