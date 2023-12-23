package com.seu.platform.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.seu.platform.dao.entity.RoleCfg;
import com.seu.platform.dao.entity.UserCfg;
import com.seu.platform.dao.service.RoleCfgService;
import com.seu.platform.dao.service.UserCfgService;
import com.seu.platform.model.entity.Login;
import com.seu.platform.model.entity.Result;
import com.seu.platform.model.vo.UserInfo;
import com.seu.platform.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-08-14 22:30
 */
@RequiredArgsConstructor
@RestController
public class PermissionController {
    private static Map<String, UserInfo> cache = new HashMap<>();
    private final UserCfgService userCfgService;
    private final RoleCfgService roleCfgService;

    @GetMapping("/api/test")
    public String test() {
        return "test";
    }

    @GetMapping("/api/getUserInfo")
    public Result<UserInfo> getUserInfo(@RequestHeader("Authorization") String token) {
        String trueToken = token.replace("store", "")
                .replace("line", "");
        UserInfo userInfo = cache.get(trueToken);
        List<Integer> roleIds = new ArrayList<>();
        if (token.contains("line")) {
            if (token.contains("admin")) {
                roleIds.add(1);
            }
            roleIds.add(3);
        } else if (token.contains("store")) {
            roleIds.add(2);
        } else {
            return Result.success(userInfo);
        }
        LambdaQueryWrapper<RoleCfg> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(true, RoleCfg::getId, roleIds);
        List<RoleCfg> list = roleCfgService.list(queryWrapper1);
        List<UserInfo.Role> roles = list.stream()
                .map(t -> BeanUtil.convertBean(t, UserInfo.Role.class))
                .collect(Collectors.toList());
        userInfo.setRoles(roles);
        userInfo.setToken(token);
        userInfo.setUserId(8);
        userInfo.setHomePath(list.get(0).getHomePath());
        return Result.success(userInfo);
    }

    @PostMapping("/api/login")
    public Result<UserInfo> login(@RequestBody @Validated Login login) {
        LambdaQueryWrapper<UserCfg> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserCfg::getUsername, login.getUsername())
                .eq(UserCfg::getPassword, login.getPassword());
        UserCfg user = userCfgService.getOne(queryWrapper);
        if (Objects.isNull(user)) {
            return Result.fail("用户名或密码错误");
        }
        UserInfo userInfo = BeanUtil.convertBean(user, UserInfo.class);
        String[] ids = user.getRoleId().split(",");
        LambdaQueryWrapper<RoleCfg> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(true, RoleCfg::getId, Arrays.asList(ids));
        List<RoleCfg> list = roleCfgService.list(queryWrapper1);
        List<UserInfo.Role> roles = list.stream()
                .map(t -> BeanUtil.convertBean(t, UserInfo.Role.class))
                .collect(Collectors.toList());
        userInfo.setRoles(roles);
        userInfo.setDesc(user.getDescription());
        userInfo.setUserId(user.getId());
        cache.put(userInfo.getToken(), userInfo);
        return Result.success(userInfo);
    }

    @GetMapping("/api/logout")
    public Result<String> logout(@RequestHeader("Authorization") String token) {
        String trueToken = token.replace("store", "")
                .replace("line", "");
        cache.put(trueToken, null);
        return Result.success("登出成功");
    }

}
