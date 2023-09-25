package com.seu.platform.exa.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-25 22:37
 */
@Data
public class ExaPointResponse {
    @JsonProperty("Result")
    @JSONField(name = "Result")
    private Integer result;

    @JsonProperty("VariablesJson")
    @JSONField(name = "VariablesJson")
    private String variablesJson;
}
