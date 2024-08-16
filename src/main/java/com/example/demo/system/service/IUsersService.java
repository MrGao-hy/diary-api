package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Location;
import com.example.demo.system.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-11-18
 */
public interface IUsersService extends IService<Users> {

    /**
     * 登录
     * */
    Result loginService(Users users);

    /**
     * 注册用户
     * */
    Result registerService(Users users);

    /**
     * 个人用户信息
     * */
    Result userInfoService(Users users);

    /**
     * 编辑用户信息
     * */
    Result editInfoService(Users users);

    /**
     * 注销账号
     * */
    Result unsubscribeService(Users users);

    /**
     * 条件搜索用户
     * */
    Result searchUserService(Users users);

    /**
     * 分页搜索所有用户信息
     * */
    Result<Page<Users>> getUserListService(Page<Users> page);

    /**
     * 导出所有用户数据
     * */
    Result exportUserListService(HttpServletResponse response);

    /**
     * 修改密码
     * */
    Result changePasswordService(Users users);
}
