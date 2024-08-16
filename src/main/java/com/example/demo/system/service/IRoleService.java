package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-07
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查询权限列表
     * */
    Result getRoleListService();
}
