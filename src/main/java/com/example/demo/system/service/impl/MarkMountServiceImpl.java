package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.MarkMount;
import com.example.demo.system.entity.MarkReply;
import com.example.demo.system.entity.Users;
import com.example.demo.system.mapper.MarkMountMapper;
import com.example.demo.system.mapper.MarkReplyMapper;
import com.example.demo.system.service.IMarkMountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.system.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    @Autowired
    private MarkReplyMapper markReplyMapper;

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
        queryWrapper.orderByDesc(MarkMount::getCreateTime);

        try {
            List<MarkMount> data = list(queryWrapper);
            Users users = new Users();

            for (MarkMount item : data) {
                LambdaQueryWrapper<MarkReply> queryMarkWrapper = new LambdaQueryWrapper<>();
                queryMarkWrapper.eq(MarkReply::getMarkId, item.getId());

                Users.UserInfoVo userInfo = usersService.queryUserInfoService(users.setId(item.getUserId()));
                Long count = markReplyMapper.selectCount(queryMarkWrapper);
                item.setUserInfo(userInfo);
                item.setAllReply(count);
            }
            return Result.success(data,"成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    @Override
    public Result<Boolean> deleteCommentService(MarkMount markMount) {
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<MarkMount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarkMount::getId, markMount.getId());
        MarkMount oneMark = getOne(queryWrapper);
        if(!oneMark.getUserId().equals(userId)) {
            return Result.fail(StatusCode.User_Not_Delete.getValue(), StatusCode.User_Not_Delete.getDescription());
        }
        queryWrapper.eq(MarkMount::getUserId, userId);

        try {
            boolean isDel = remove(queryWrapper);

            if(isDel) {
                return Result.success(true,"删除成功");
            } else {
                return Result.success(false,"删除失败");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }
}
