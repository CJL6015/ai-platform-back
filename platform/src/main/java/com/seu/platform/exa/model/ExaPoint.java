package com.seu.platform.exa.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-09-25 22:20
 */
@Data
public class ExaPoint {
    @JsonProperty("Name")
    @JSONField(name = "Name")
    private String name;

    @JsonProperty("ID")
    @JSONField(name = "ID")
    private int id;

    @JsonProperty("Address")
    @JSONField(name = "Address")
    private String address;

    @JsonProperty("Type")
    @JSONField(name = "Type")
    private int type;

    @JsonProperty("Description")
    @JSONField(name = "Description")
    private String description;

    @JsonProperty("Unit")
    @JSONField(name = "Unit")
    private String unit;

    @JsonProperty("Comment")
    @JSONField(name = "Comment")
    private String comment;

    @JsonProperty("EnablePersistence")
    @JSONField(name = "EnablePersistence")
    private boolean enablePersistence;

    @JsonProperty("PreAggregation")
    @JSONField(name = "PreAggregation")
    private int preAggregation;
}
