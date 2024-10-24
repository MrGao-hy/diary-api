package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.CollectMount;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.mapper.CollectMountMapper;
import com.example.demo.system.service.ICollectMountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.system.service.IMountService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class CollectMountServiceImpl extends ServiceImpl<CollectMountMapper, CollectMount> implements ICollectMountService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IMountService mountService;

    @Override
    public Result<Boolean> collectMountService(CollectMount collectMount) {
        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<CollectMount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectMount::getMountId, collectMount.getMountId()).eq(CollectMount::getUserId, userId);

        try {
            long cou = count(queryWrapper);

            if(cou == 1) {
                remove(queryWrapper);
                return Result.success(false, "取消收藏");
            } else {
                collectMount.setUserId(userId);
                save(collectMount);
                return Result.success(true, "收藏成功");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }

    @Override
    public Result<Boolean> queryIsCollectService(CollectMount collectMount) {
        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<CollectMount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectMount::getMountId, collectMount.getMountId()).eq(CollectMount::getUserId, userId);

        try {
            long cou = count(queryWrapper);
            if(cou == 1) {
                return Result.success(true, "查询成功");
            } else {
                return Result.success(false, "查询成功");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }

    @Override
    public Result<List<Mount>> queryIsCollectListService(CollectMount collectMount) {
        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<CollectMount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CollectMount::getUserId, userId);

        try {
            List<CollectMount> collectList = list(queryWrapper);
            List<Mount> list = new ArrayList<>();

            for (CollectMount item : collectList) {
                Mount data = mountService.queryCollectMountService(item.getMountId());
                list.add(data);
            }
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }
}
