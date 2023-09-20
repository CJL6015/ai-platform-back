package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.model.vo.PointStatisticVO;

import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【point_cfg(测点基表)】的数据库操作Service
 * @createDate 2023-09-11 22:24:42
 */
public interface PointCfgService extends IService<PointCfg> {
    /**
     * 分页查询测点统计结果
     *
     * @param lineId   生产线id
     * @param pageNum  页数
     * @param pageSize 页大小
     * @return 该页数据
     */
    Page<PointStatisticVO> getPointStatisticPage(Integer lineId, int pageNum, int pageSize);

    /**
     * 查询测点统计结果
     *
     * @param lineId 生产线id
     * @return 数据
     */
    List<PointStatisticVO> getPointStatistic(Integer lineId);
}
