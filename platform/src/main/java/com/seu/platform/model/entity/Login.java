package com.seu.platform.model.entity;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-12-17 13:28
 */
@Data
public class Login {
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;
}
