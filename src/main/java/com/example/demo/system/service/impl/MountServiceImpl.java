package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.CollectMount;
import com.example.demo.system.entity.MarkMount;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.mapper.CollectMountMapper;
import com.example.demo.system.mapper.MarkMountMapper;
import com.example.demo.system.mapper.MountMapper;
import com.example.demo.system.service.IMountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-19
 */
@Service
public class MountServiceImpl extends ServiceImpl<MountMapper, Mount> implements IMountService {

    @Autowired
    private CollectMountMapper collectMountMapper;
    @Autowired
    private MarkMountMapper markMountMapper;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public Result<String> createMountService(Mount mount) {

        if(mount.getName().isEmpty()) {
            return Result.fail(StatusCode.PARAM_NOT_NULL.getValue(), "【name】" + StatusCode.PARAM_NOT_NULL.getDescription());
        }
        if(mount.getAltitude().isNaN()) {
            return Result.fail(StatusCode.PARAM_NOT_NULL.getValue(), "【altitude】" + StatusCode.PARAM_NOT_NULL.getDescription());
        }

        try {
            // 更新或者创建
            LambdaUpdateWrapper<Mount> updateWrapper = Wrappers.lambdaUpdate();
            updateWrapper.eq(Mount::getName, mount.getName());
            boolean isSave = saveOrUpdate(mount, updateWrapper);
            if(isSave) {
                return Result.success();
            } else {
                return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription());
        }

    }

    @Override
    public Result<Page<Mount>> queryMountListService(Page<Mount> page) {
        // 用户id
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<Mount> queryWrapper = new LambdaQueryWrapper<>();

        try {

            queryWrapper.orderByDesc(Mount::getAltitude);
            Page<Mount> mountList = page(page, queryWrapper);

            for (Mount item : mountList.getRecords()) {
                LambdaQueryWrapper<MarkMount> queryMarkWrapper = new LambdaQueryWrapper<>();
                LambdaQueryWrapper<CollectMount> queryCollectWrapper = new LambdaQueryWrapper<>();
                queryCollectWrapper.eq(CollectMount::getMountId, item.getId());
                queryMarkWrapper.eq(MarkMount::getMountId, item.getId());
                Long markCount =  markMountMapper.selectCount(queryMarkWrapper);
                Long collectCount =  collectMountMapper.selectCount(queryCollectWrapper);
                item.setMarkCount(markCount);
                item.setCollectCount(collectCount);

                queryCollectWrapper.eq(CollectMount::getUserId, userId);
                boolean isCollect = collectMountMapper.exists(queryCollectWrapper);
                item.setCollect(isCollect);
            }
            return Result.success(mountList, "查询成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }

    @Override
    public Result<Mount> queryMountDetailService(Mount mount) {

        // 用户id
        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<Mount> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Mount::getId, mount.getId());
        LambdaQueryWrapper<MarkMount> queryMarkWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<CollectMount> queryCollectWrapper = new LambdaQueryWrapper<>();

        try {
            Mount queryOne = getOne(queryWrapper);

            if(queryOne != null) {
                queryCollectWrapper.eq(CollectMount::getMountId, queryOne.getId());
                queryMarkWrapper.eq(MarkMount::getMountId, queryOne.getId());
                Long markCount =  markMountMapper.selectCount(queryMarkWrapper);
                Long collectCount =  collectMountMapper.selectCount(queryCollectWrapper);
                queryOne.setMarkCount(markCount);
                queryOne.setCollectCount(collectCount);

                queryCollectWrapper.eq(CollectMount::getUserId, userId);
                boolean isCollect = collectMountMapper.exists(queryCollectWrapper);
                queryOne.setCollect(isCollect);

                return Result.success(queryOne, "查询成功");
            } else {
                return Result.fail(StatusCode.NOT_DATA.getValue(), StatusCode.NOT_DATA.getDescription());
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }

    }

    @Override
    public Mount queryCollectMountService(Long id) {

        try {
            Mount mount = getById(id);
            LambdaQueryWrapper<MarkMount> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MarkMount::getMountId, id);
            Long markCount =  markMountMapper.selectCount(queryWrapper);

            LambdaQueryWrapper<CollectMount> queryCollectWrapper = new LambdaQueryWrapper<>();
            queryCollectWrapper.eq(CollectMount::getMountId, id);
            Long collectCount =  collectMountMapper.selectCount(queryCollectWrapper);

            mount.setCollectCount(collectCount);
            mount.setMarkCount(markCount);
            return mount;
        } catch (Exception e) {
            return new Mount();
        }
    }
}
