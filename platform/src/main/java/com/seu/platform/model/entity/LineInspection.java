package com.seu.platform.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class LineInspection {
    private Integer LineId;

    private Date time;

    private Integer interval;

    private Integer mode;

    private String cameraIp;
}
