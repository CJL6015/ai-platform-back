package com.seu.platform.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-14 22:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo implements Serializable {
    private Integer userId;
    private String username;
    private String realName;
    private String avatar;
    private String desc;
    private String token;
    private String homePath;
    private List<Role> roles;

    @Data
    public static class Role {
        private String roleName;

        private String value;
    }

}
