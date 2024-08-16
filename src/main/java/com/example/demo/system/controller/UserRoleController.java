package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.UserRole;
import com.example.demo.system.service.IUserRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    private IUserRoleService userRoleService;

    @ApiOperation(value = "查询权限")
    @GetMapping("/query/limit")
    public Result queryUserRole() {
        return userRoleService.queryUserRoleService();
    }

    @ApiOperation(value = "设置权限")
    @PostMapping("/set/limit")
    public Result serUserRole(@RequestBody UserRole userRole) {
        return userRoleService.bindRoleService(userRole);
    }
}
