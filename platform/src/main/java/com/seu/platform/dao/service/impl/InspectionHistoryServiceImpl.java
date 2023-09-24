package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.InspectionHistory;
import com.seu.platform.dao.mapper.InspectionHistoryMapper;
import com.seu.platform.dao.service.InspectionHistoryService;
import com.seu.platform.model.vo.InspectionHistoryDataVO;
import com.seu.platform.model.vo.InspectionHistoryVO;
import com.seu.platform.model.vo.TimeValueChartVO;
import com.seu.platform.util.BeanUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 陈小黑
 * @description 针对表【inspection_history(巡检历史)】的数据库操作Service实现
 * @createDate 2023-09-11 22:17:07
 */
@Service
public class InspectionHistoryServiceImpl extends ServiceImpl<InspectionHistoryMapper, InspectionHistory>
        implements InspectionHistoryService {

    private static final int STEP = 60 * 60 * 1000;


    @Override
    public InspectionHistoryDataVO getInspectionHistoryValue(Integer lineId, Date st, Date et) {
        LambdaQueryWrapper<InspectionHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InspectionHistory::getLineId, lineId)
                .ge(Objects.nonNull(st), InspectionHistory::getSt, st)
                .le(Objects.nonNull(et), InspectionHistory::getEt, et)
                .orderByAsc(InspectionHistory::getSt);
        List<InspectionHistory> histories = list(queryWrapper);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> timestamps = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        values.add(null);
        long t1 = st.getTime();
        List<InspectionHistoryVO> tableData = new ArrayList<>();
        for (InspectionHistory history : histories) {
            long start = history.getSt().getTime();
            fillValue(timestamps, values, t1, start, null);
            long end = history.getEt().getTime();
            fillValue(timestamps, values, start, end, 1);
            t1 = end;
            tableData.add(BeanUtil.convertBean(history, InspectionHistoryVO.class));
        }
        fillValue(timestamps, values, t1, et.getTime(), null);
        TimeValueChartVO vo = new TimeValueChartVO(timestamps, values);
        return new InspectionHistoryDataVO(vo, tableData);
    }

    private void fillValue(List<String> timestamps, List<Integer> values, long st, long et, Integer value) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (long t = st; t <= et; t += STEP) {
            timestamps.add(dateFormat.format(t));
            values.add(value);
        }
    }
}




