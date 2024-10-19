package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.DiaryText;
import com.example.demo.system.entity.Role;
import com.example.demo.system.entity.UserRole;
import com.example.demo.system.mapper.RoleMapper;
import com.example.demo.system.mapper.UserRoleMapper;
import com.example.demo.system.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-01
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RoleMapper roleMapper;


    /**
     * 查询个人权限
     * */
    @Override
    public Result<List<Role>> queryUserRoleService() {

        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<UserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserRole::getUserId, userId);

        try {
            List<UserRole> limits = list(wrapper);

            List<Role> roleList = new ArrayList<>();
            for (UserRole userRole : limits) {

                LambdaQueryWrapper<Role> wrapper1 = Wrappers.lambdaQuery();
                wrapper1.eq(Role::getId, userRole.getRoleId());
                Role data = roleMapper.selectOne(wrapper1);

                roleList.add(data);
            }
            return Result.success(roleList);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    /**
     * 绑定权限管理
     * */
    @Override
    public Result<String> bindRoleService(UserRole userRole) {

        LambdaUpdateWrapper<UserRole> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserRole::getUserId, userRole.getUserId());

        List<UserRole> list = new ArrayList<>();
        if((userRole.getRoleId() == null || userRole.getRoleId().isEmpty()) && userRole.getRoleIds() == null) {
            userRole.setRoleId("4");
            list.add(userRole);
        } else {
            remove(updateWrapper);
            for (String item : userRole.getRoleIds()) {
                UserRole newUserRole = new UserRole();
                newUserRole.setUserId(userRole.getUserId());
                newUserRole.setRoleId(item);
                list.add(newUserRole);
            }
        }

        try {
            boolean isSave = saveBatch(list);
            if(isSave) {
                return Result.success("绑定成功");
            } else {
                return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    /**
     * 查询用户名称
     * */
    public List<Role> queryRoleService(UserRole userRole) {
        List<Role> roles = new ArrayList<>();
        LambdaUpdateWrapper<UserRole> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserRole::getUserId, userRole.getUserId());

        try {
            List<UserRole> list = list(wrapper);
            for (UserRole item : list) {
                LambdaUpdateWrapper<Role> wrapper2 = new LambdaUpdateWrapper<>();
                wrapper2.eq(Role::getId, item.getRoleId());
                Role roleTemp = roleMapper.selectOne(wrapper2);
                roles.add(roleTemp);
            }
            return roles;
        } catch (Exception e) {
            return roles;
        }
    }
}
