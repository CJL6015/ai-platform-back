package com.seu.platform.service;

import com.seu.platform.model.vo.RelationVO;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 15:19
 */
public interface RuleService {

    RelationVO getSupportEquipment(Integer lineId, Double limit);

    RelationVO getSupportPoint(Integer lineId, Double limit);
}
