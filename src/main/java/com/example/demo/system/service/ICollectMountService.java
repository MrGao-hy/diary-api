package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.CollectMount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.system.entity.Mount;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-22
 */
public interface ICollectMountService extends IService<CollectMount> {

    Result<Boolean> collectMountService(CollectMount collectMount);

    Result<Boolean> queryIsCollectService(CollectMount collectMount);

    Result<Page<Mount>> queryIsCollectListService(Page<CollectMount> page);
}
