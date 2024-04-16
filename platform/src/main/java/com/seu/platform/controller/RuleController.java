package com.seu.platform.controller;

import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.RelationCacheVO;
import com.seu.platform.model.vo.RelationVO;
import com.seu.platform.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 15:19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rule")
public class RuleController {
    private final RuleService ruleService;
    private final CacheManager cacheManager;

    @GetMapping("/equipment/{lineId}")
    public Result<RelationVO> getEquipment(@PathVariable Integer lineId, Double limit) {
        Cache cache = cacheManager.getCache("rule-getEquipment");
        RelationVO supportEquipment = cache.get(lineId + "-" + limit, RelationVO.class);
        if (supportEquipment == null) {
            log.info("未获取到缓存");
            supportEquipment = ruleService.getSupportEquipment(lineId, limit);
            cache.put(lineId + "-" + limit, supportEquipment);
        }
        return Result.success(supportEquipment);
    }

    @GetMapping("/point/{lineId}")
    public Result<RelationVO> getPoint(@PathVariable Integer lineId, Double limit) {
        Cache cache = cacheManager.getCache("rule-getPoint");
        RelationVO supportPoint = cache.get(lineId + "-" + limit, RelationVO.class);
        if (supportPoint == null) {
            log.info("未获取到缓存");
            supportPoint = ruleService.getSupportPoint(lineId, limit);
            cache.put(lineId + "-" + limit, supportPoint);
        }
        return Result.success(supportPoint);
    }

    @PostMapping("/equipment")
    public Result<String> saveEquipment(@RequestBody RelationCacheVO relationCacheVO) {
        putEquipmentCache(relationCacheVO.getLineId(), relationCacheVO.getLimit(), relationCacheVO.getRelation());
        return Result.success();
    }

    @PostMapping("/point")
    public Result<String> savePoint(@RequestBody RelationCacheVO relationCacheVO) {
        putPointCache(relationCacheVO.getLineId(), relationCacheVO.getLimit(), relationCacheVO.getRelation());
        return Result.success();
    }

    public void putPointCache(Integer lineId, Double limit, RelationVO vo) {
        Cache cache = cacheManager.getCache("rule-getPoint");
        cache.put(lineId + "-" + limit, vo);
    }

    public void putEquipmentCache(Integer lineId, Double limit, RelationVO vo) {
        log.info("设备相关性缓存添加,key:{},{}", lineId + "-" + limit, vo);
        Cache cache = cacheManager.getCache("rule-getEquipment");
        cache.put(lineId + "-" + limit, vo);
    }
}
