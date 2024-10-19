package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.ApplyList;
import com.example.demo.system.mapper.ApplyListMapper;
import com.example.demo.system.service.IApplyListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-05-07
 */
@Service
public class ApplyListServiceImpl extends ServiceImpl<ApplyListMapper, ApplyList> implements IApplyListService {

    /**
     * 获取工具应用列表
     * */
    @Override
    public Result getApplyListService() {
        try {
            List<ApplyList> applyLists = list();
            return Result.success(applyLists);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(),StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    /**
     * 添加工具应用
     * */
    @Override
    public Result addApplyService(ApplyList applyList) {
        boolean isAdd = save(applyList);
        if(isAdd) {
            return Result.success("创建成功");
        } else {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(),StatusCode.SQL_STATUS_ERROR.getDescription());
        }
    }

    /**
     * 更新工具应用状态
     * */
    @Override
    public Result changeApplyService(ApplyList applyList) {
        LambdaUpdateWrapper<ApplyList> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ApplyList::getId, applyList.getId());
        // 插入想要修改的值
        wrapper.set(Objects.nonNull(applyList.getTitle()), ApplyList::getTitle, applyList.getTitle());
        wrapper.set(Objects.nonNull(applyList.getUrl()), ApplyList::getUrl, applyList.getUrl());
        wrapper.set(Objects.nonNull(applyList.getStartDate()), ApplyList::getStartDate, applyList.getStartDate());
        wrapper.set(Objects.nonNull(applyList.getEndDate()), ApplyList::getEndDate, applyList.getEndDate());
        wrapper.set(Objects.nonNull(applyList.getIsFinish()), ApplyList::getIsFinish, applyList.getIsFinish());
        if (applyList.getIsFinish()) {
            wrapper.set(ApplyList::getState, "异常");
        } else {
            wrapper.set(ApplyList::getState, "正常");
        }
        boolean isUpdate = update(wrapper);
        if(isUpdate) {
            return Result.success("更新成功");
        } else {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(),StatusCode.SQL_STATUS_ERROR.getDescription());
        }
    }

    @Override
    public Result deleteApplyService(List<String> ids) {
        boolean del = removeByIds(ids);
        if(del) {
            return Result.success("删除成功");
        } else {
            return Result.success("删除失败");
        }
    }
}
