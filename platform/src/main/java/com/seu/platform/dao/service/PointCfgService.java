package com.seu.platform.dao.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.model.vo.PointConfigVO;
import com.seu.platform.model.vo.PointStatisticVO;
import com.seu.platform.model.vo.PointTrendVO;
import com.seu.platform.model.vo.TimeRange;

import java.util.List;
import java.util.Map;

/**
 * @author 陈小黑
 * @description 针对表【point_cfg(测点基表)】的数据库操作Service
 * @createDate 2023-09-24 21:45:47
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
     * @param lineId    生产线id
     * @param timeRange 时间
     * @return 数据
     */
    List<PointStatisticVO> getPointStatistic(Integer lineId, TimeRange timeRange);

    /**
     * 获取测点数据
     *
     * @param lineId 生产线id
     * @return 测点数据
     */
    List<PointConfigVO> getPointList(Integer lineId);

    /**
     * 获取测点趋势
     *
     * @param name  点号名称
     * @param start 开始时间
     * @param end   结束时间
     * @return 测点趋势
     * @throws Exception 异常
     */
    PointTrendVO getPointTrend(String name, Long start, Long end) throws Exception;

    /**
     * 获取点号限值
     *
     * @param names 点号
     * @return 限值
     */
    Map<String, Double[]> getPointLimits(String names);
}
