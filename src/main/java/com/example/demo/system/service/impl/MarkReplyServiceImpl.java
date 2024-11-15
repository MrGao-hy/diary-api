package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.MarkMount;
import com.example.demo.system.entity.MarkReply;
import com.example.demo.system.entity.PageRequest;
import com.example.demo.system.entity.Users;
import com.example.demo.system.mapper.MarkReplyMapper;
import com.example.demo.system.service.IMarkReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.system.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-11-14
 */
@Service
public class MarkReplyServiceImpl extends ServiceImpl<MarkReplyMapper, MarkReply> implements IMarkReplyService {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public Result<String> markReplyService(MarkReply markReply) {
        String userId = hostHolder.getUser().getId();
        markReply.setUserId(userId);
        try {
            save(markReply);
            return Result.success("评论成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    @Override
    public Result<Page<MarkReply>> markReplyListService(PageRequest<MarkReply> param) {
        LambdaQueryWrapper<MarkReply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MarkReply::getMarkId, param.getIn().getMarkId());
        queryWrapper.orderByDesc(MarkReply::getCreateTime);

        try {
            Page<MarkReply> list = page(param, queryWrapper);
            Users users = new Users();

            for (MarkReply item : list.getRecords()) {
                Users.UserInfoVo userInfo = usersService.queryUserInfoService(users.setId(item.getUserId()));
                item.setUserInfo(userInfo);
            }
            return Result.success(list,"成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }
}
