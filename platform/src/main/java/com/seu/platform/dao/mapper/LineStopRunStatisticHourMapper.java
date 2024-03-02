package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.LineStopRunStatisticHour;
import com.seu.platform.model.dto.CountIdDTO;
import com.seu.platform.model.dto.LineRunDTO;
import com.seu.platform.model.dto.RunTimeDTO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【line_stop_run_statistic_hour】的数据库操作Mapper
 * @createDate 2023-12-23 13:23:54
 * @Entity com.seu.platform.dao.entity.LineStopRunStatisticHour
 */
public interface LineStopRunStatisticHourMapper extends BaseMapper<LineStopRunStatisticHour> {

    RunTimeDTO getRunTime(Date st, Date et);

    RunTimeDTO getRunTimeByLineId(Integer lineId, Date st, Date et);

    List<LineRunDTO> getLineRun(Integer lineId, Date st, Date et);

    List<CountIdDTO> getRunHour(Date st, Date et);
}




