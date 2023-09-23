package com.seu.platform.exa.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-23 20:44
 */
@Data
public class ValueFloat {
    @JsonProperty("Result")
    @JSONField(name = "Result")
    private Integer result;

    @JsonProperty("Value")
    @JSONField(name = "Value")
    private Float value;
}
