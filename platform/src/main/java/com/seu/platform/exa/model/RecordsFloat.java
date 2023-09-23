package com.seu.platform.exa.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 20:30
 */
@Data
public class RecordsFloat {
    @JsonProperty("Result")
    @JSONField(name = "Result")
    private Integer result;

    @JsonProperty("Timestamps")
    @JSONField(name = "Timestamps")
    private List<Long> timestamps;

    @JsonProperty("Values")
    @JSONField(name = "Values")
    private List<Float> values;
}
