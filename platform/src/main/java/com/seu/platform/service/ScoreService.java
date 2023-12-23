package com.seu.platform.service;

import com.seu.platform.model.vo.TimeRange;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-19 19:22
 */
public interface ScoreService {

    List<List<Object>> getScoreTrend(Integer lineId, TimeRange timeRange);



}
