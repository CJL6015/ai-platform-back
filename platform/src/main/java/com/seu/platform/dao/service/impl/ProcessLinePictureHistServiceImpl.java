package com.seu.platform.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seu.platform.dao.entity.ProcessLinePictureHist;
import com.seu.platform.dao.mapper.ProcessLinePictureHistMapper;
import com.seu.platform.dao.service.ProcessLinePictureHistService;
import com.seu.platform.model.vo.DetectionResultVO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 陈小黑
 * @description 针对表【process_line_picture_hist】的数据库操作Service实现
 * @createDate 2023-10-28 10:48:15
 */
@Service
public class ProcessLinePictureHistServiceImpl extends ServiceImpl<ProcessLinePictureHistMapper, ProcessLinePictureHist>
        implements ProcessLinePictureHistService {

    @Override
    public List<ProcessLinePictureHist> getPendingChecks(int count) {
        return getBaseMapper().getPendingChecks(count);
    }

    @Override
    public List<DetectionResultVO> getDetectionResult(List<String> ips, Date time) {
        return getBaseMapper().getDetectionResult(ips, time);
    }
}




