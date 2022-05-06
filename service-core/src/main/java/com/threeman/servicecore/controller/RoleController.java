package com.threeman.servicecore.controller;

import com.threeman.common.exception.CreateException;
import com.threeman.security.service.UserRoleService;
import com.threeman.servicecore.entity.User;
import com.threeman.servicecore.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author songjing
 * @version 1.0
 * @date 2022/3/1 16:15
 */
@RestController
@Slf4j
public class RoleController {

    @Autowired
    IUserService userService;

    @Autowired
    UserRoleService userRoleService;

    /**
     * 给用户添加角色
     *
     * @return boolean
     */
    @PutMapping("/addUserRole")
    public boolean insertUserRole(@RequestBody Map<String, Object> param) {
        String username = param.get("username").toString();
        ArrayList<String> roleNameList = new ArrayList<>();
        Object roleNames = param.get("roleNames");
        if (roleNames == null || "".equals(roleNames)) {
            throw new CreateException("角色信息不能为空");
        }
        if (roleNames instanceof ArrayList) {
            roleNameList.addAll((List<String>) roleNames);
        }
        User userInfo = userService.getUserInfo(username);
        if (userInfo == null) {
            throw new CreateException("查不到用户信息");
        }
        return userRoleService.insertUserRole(userInfo.getUserId(), roleNameList);
    }
}
