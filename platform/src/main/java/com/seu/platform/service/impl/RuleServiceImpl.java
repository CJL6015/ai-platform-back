package com.seu.platform.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.algorithm.Apriori;
import com.seu.platform.dao.entity.EquipmentCfg;
import com.seu.platform.dao.entity.PointCfg;
import com.seu.platform.dao.mapper.PointStatisticHourMapper;
import com.seu.platform.dao.service.EquipmentCfgService;
import com.seu.platform.dao.service.PointCfgService;
import com.seu.platform.model.vo.RelationTableVO;
import com.seu.platform.model.vo.RelationVO;
import com.seu.platform.service.RuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-09 15:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {
    private final PointStatisticHourMapper pointStatisticHourMapper;

    private final EquipmentCfgService equipmentCfgService;

    private final PointCfgService pointCfgService;

    @Override
    public RelationVO getSupportEquipment(Integer lineId, Double limit) {
        LambdaQueryWrapper<EquipmentCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EquipmentCfg::getLineId, lineId)
                .orderBy(Boolean.TRUE, Boolean.TRUE, EquipmentCfg::getId);
        List<EquipmentCfg> list = equipmentCfgService.list(queryWrapper);
        Map<String, String> map = new HashMap<>();
        list.forEach(t -> map.put(t.getId().toString(), t.getEquipmentDescription().trim()));
        Date et = new Date();
        DateTime st = DateUtil.offsetMonth(et, -1);
        List<String> relationEquipment = pointStatisticHourMapper.getRelationEquipment(lineId, st, et);
        List<Set<String>> data = relationEquipment.stream()
                .map(t -> CollUtil.newHashSet(t.split(",")))
                .collect(Collectors.toList());
        Apriori apriori = new Apriori(data, 0.1, limit);
        List<List<Object>> lists = apriori.associationRules();
        List<Object[]> group = apriori.getGroup();
        List<List<String>> groupData = new ArrayList<>();
        for (Object[] p : group) {
            List<String> l = new ArrayList<>();
            Set<String> set = (Set<String>) p[0];
            String subEquipment = set.stream().map(map::get).collect(Collectors.joining(","));
            l.add(subEquipment);
            l.add(p[1].toString());
            groupData.add(l);
        }
        groupData = groupData.subList(0, Math.min(groupData.size(), 3));
        List<RelationTableVO> table = lists.stream()
                .map(t -> {
                    Set<String> set = (Set<String>) t.get(0);
                    Set<String> target = (Set<String>) t.get(1);
                    String subEquipment = set.stream().map(map::get).collect(Collectors.joining(","));
                    String targetEquipment = target.stream().map(map::get).collect(Collectors.joining(","));
                    return RelationTableVO.builder()
                            .sub(subEquipment)
                            .target(targetEquipment)
                            .conf(t.get(2).toString())
                            .support(t.get(3).toString())
                            .relevant(t.get(4).toString())
                            .build();
                })
                .collect(Collectors.toList());
        return RelationVO.builder()
                .table(table)
                .groupData(groupData)
                .build();
    }

    @Override
    public RelationVO getSupportPoint(Integer lineId, Double limit) {
        LambdaQueryWrapper<PointCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PointCfg::getLineId, lineId)
                .orderBy(Boolean.TRUE, Boolean.TRUE, PointCfg::getId);
        List<PointCfg> list = pointCfgService.list(queryWrapper);
        Map<String, String> map = new HashMap<>();
        list.forEach(t -> map.put(t.getPointId().toString(), t.getDescription().trim()));
        Date et = new Date();
        DateTime st = DateUtil.offsetMonth(et, -1);
        List<String> relationEquipment = pointStatisticHourMapper.getRelationPoint(lineId, st, et);
        List<Set<String>> data = relationEquipment.stream()
                .map(t -> CollUtil.newHashSet(t.split(",")))
                .collect(Collectors.toList());
        Apriori apriori = new Apriori(data, 0.1, limit);
        List<List<Object>> lists = apriori.associationRules();
        List<Object[]> group = apriori.getGroup();
        List<List<String>> groupData = new ArrayList<>();
        for (Object[] p : group) {
            List<String> l = new ArrayList<>();
            Set<String> set = (Set<String>) p[0];
            String subEquipment = set.stream().map(map::get).collect(Collectors.joining(","));
            l.add(subEquipment);
            l.add(p[1].toString());
            groupData.add(l);
        }
        groupData = groupData.subList(0, Math.min(groupData.size(), 3));
        List<RelationTableVO> table = lists.stream()
                .map(t -> {
                    Set<String> set = (Set<String>) t.get(0);
                    Set<String> target = (Set<String>) t.get(1);
                    String subEquipment = set.stream().map(map::get).collect(Collectors.joining(","));
                    String targetEquipment = target.stream().map(map::get).collect(Collectors.joining(","));
                    return RelationTableVO.builder()
                            .sub(subEquipment)
                            .target(targetEquipment)
                            .conf(t.get(2).toString())
                            .support(t.get(3).toString())
                            .relevant(t.get(4).toString())
                            .build();
                })
                .collect(Collectors.toList());
        return RelationVO.builder()
                .table(table)
                .groupData(groupData)
                .build();
    }
}
