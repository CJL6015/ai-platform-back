package com.seu.platform.service.impl;

import com.seu.platform.dao.mapper.EquipmentCfgMapper;
import com.seu.platform.model.dto.EquipmentTrendDTO;
import com.seu.platform.model.vo.EquipmentTrendVO;
import com.seu.platform.service.BenchmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 19:32
 */
@Service
@RequiredArgsConstructor
public class BenchmarkServiceImpl implements BenchmarkService {

    private final EquipmentCfgMapper equipmentCfgMapper;

    @Override
    public EquipmentTrendVO getEquipmentTrend(Date st, Date et) {
        List<EquipmentTrendDTO> equipmentTrend = equipmentCfgMapper.getEquipmentTrend(st, et);
        List<String> equipments = new ArrayList<>();
        List<List<Object[]>> data = new ArrayList<>();
        List<Object[]> value = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        List<Integer> equipmentMax = new ArrayList<>();
        data.add(value);
        String name = equipmentTrend.get(0).getEquipmentName().trim();
        int count = 0;
        int max = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (EquipmentTrendDTO dto : equipmentTrend) {
            String equipmentName = dto.getEquipmentName().trim();
            if (!equipments.contains(equipmentName)) {
                equipments.add(equipmentName);
            }
            if (!name.equals(equipmentName)) {
                value = new ArrayList<>();
                data.add(value);
                name = equipmentName;
                counts.add(count);
                count = 0;
                equipmentMax.add(max);
                max = 0;
            }
            Integer dtoCount = dto.getCount();
            max = Math.max(max, dtoCount);
            count += dtoCount;
            value.add(new Object[]{dateFormat.format(dto.getTime()), dtoCount});
        }
        counts.add(count);
        equipmentMax.add(max);
        return EquipmentTrendVO.builder()
                .equipments(equipments)
                .equipmentMax(equipmentMax)
                .counts(counts)
                .values(data)
                .build();
    }
}
