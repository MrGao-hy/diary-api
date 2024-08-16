package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Role;
import com.example.demo.system.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-01
 */
public interface IUserRoleService extends IService<UserRole> {
    /**
     * 查询个人权限
     * */
    Result queryUserRoleService();

    /**
     * 绑定权限管理
     * */
    Result bindRoleService(UserRole userRole);

    List<Role> queryRoleService(UserRole userRole);
}
