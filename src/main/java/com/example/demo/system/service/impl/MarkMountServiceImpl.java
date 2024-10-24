package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.MarkMount;
import com.example.demo.system.entity.Users;
import com.example.demo.system.mapper.MarkMountMapper;
import com.example.demo.system.service.IMarkMountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.system.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-22
 */
@Service
public class MarkMountServiceImpl extends ServiceImpl<MarkMountMapper, MarkMount> implements IMarkMountService {
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IUsersService usersService;

    @Override
    public Result<String> markMountService(MarkMount markMount) {
        String userId = hostHolder.getUser().getId();

        if (markMount.getMountId() == null){
            return Result.fail(StatusCode.PARAM_NOT_NULL.getValue(), "【mountId】" + StatusCode.PARAM_NOT_NULL.getDescription());
        }
        if (markMount.getComment().isEmpty()){
            return Result.fail(StatusCode.PARAM_NOT_NULL.getValue(), "【comment】" + StatusCode.PARAM_NOT_NULL.getDescription());
        }


        try {

            markMount.setUserId(userId);
            save(markMount);
            return Result.success("评语成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }

    @Override
    public Result<List<MarkMount>> commentListService(MarkMount markMount) {
        LambdaQueryWrapper<MarkMount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarkMount::getMountId, markMount.getMountId());

        try {
            List<MarkMount> data = list(queryWrapper);
            Users users = new Users();

            for (MarkMount item : data) {
                Users.UserInfoVo userInfo = usersService.queryUserInfoService(users.setId(item.getUserId()));
                item.setUserInfo(userInfo);
            }
            return Result.success(data,"成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }
}
