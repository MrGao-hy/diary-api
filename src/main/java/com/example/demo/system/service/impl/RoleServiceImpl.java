package com.example.demo.system.service.impl;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Role;
import com.example.demo.system.mapper.RoleMapper;
import com.example.demo.system.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-07
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Result<List<Role>> getRoleListService() {
        List<Role> roleList = list();
        return Result.success(roleList, "查询成功");
    }
}
