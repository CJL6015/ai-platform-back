package com.seu.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seu.platform.dao.entity.PointInspectionHour;
import com.seu.platform.model.dto.ExceedDTO;
import com.seu.platform.model.dto.PointExceedInspectionDTO;
import com.seu.platform.model.dto.PointReportDTO;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_inspection_hour】的数据库操作Mapper
 * @createDate 2024-01-13 15:06:21
 * @Entity com.seu.platform.dao.entity.PointInspectionHour
 */
public interface PointInspectionHourMapper extends BaseMapper<PointInspectionHour> {
    /**
     * 获取点号超限统计
     *
     * @param ids 点号
     * @return dto
     */
    List<PointReportDTO> getPointReport(Integer[] ids);

    List<PointExceedInspectionDTO> getPointInspection(Integer lineId, Date st, Date et);

    ExceedDTO getExceed(Integer lineId, Date st, Date et);

    PointExceedInspectionDTO getTotalPointInspection(Integer lineId, Date st, Date et);

    List<PointExceedInspectionDTO> getPointInspectionList(Integer lineId, Date st, Date et);
}




