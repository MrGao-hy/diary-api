package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Mount;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.system.entity.PageRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-19
 */
public interface IMountService extends IService<Mount> {

    Result<String> createMountService(Mount mount);

    Result<Page<Mount>> queryMountListService(Page<Mount> page);

    Result<Mount> queryMountDetailService(Mount mount);

    /**
     * 查询收藏山接口
     * */
    Mount queryCollectMountService(Long id);
}
