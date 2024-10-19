package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.PageRequest;
import com.example.demo.system.entity.Users;
import com.example.demo.system.service.IUsersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-11-18
 */
@RestController
@RequestMapping("/user")
public class UsersController {
    @Autowired
    private IUsersService usersService;

    @ApiOperation(value = "注册用户")
    @PostMapping("/register")
    public Result setRegister(@RequestBody Users users) {
        return usersService.registerService(users);
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody Users users) {
        return usersService.loginService(users);
    }

    @ApiOperation(value = "用户信息")
    @PostMapping ("/info")
    public Result getUserInfo(@RequestBody Users users) {
        return usersService.userInfoService(users);
    }

    @ApiOperation(value = "编辑用户信息")
    @PostMapping("/editInfo")
    public Result editUserInfo(@RequestBody Users users) {
        return usersService.editInfoService(users);
    }

    @ApiOperation(value = "修改密码")
    @PostMapping("/change/password")
    public Result changePasswordApi(@RequestBody Users users) { return usersService.changePasswordService(users); }

    @ApiOperation(value = "注销用户")
    @PostMapping("/unsubscribe")
    public Result unsubscribe(@RequestBody Users users) {
        return usersService.unsubscribeService(users);
    }

    @ApiOperation(value = "查询所以用户")
    @PostMapping("/list")
    public Result getAll(@RequestBody PageRequest pageRequest) {
        return usersService.getUserListService(new Page<Users>(pageRequest.getCurrent(), pageRequest.getSize()));
    }

    @ApiOperation(value = "条件搜索用户")
    @PostMapping("/search")
    public Result searchUser(@RequestBody Users users) { return usersService.searchUserService(users);}

    @ApiOperation(value = "导出数据到excel表格")
    @GetMapping("/export")
    public Result exportUsers(HttpServletResponse response) { return usersService.exportUserListService(response); }

}
