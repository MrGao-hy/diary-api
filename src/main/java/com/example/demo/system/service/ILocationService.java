package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Location;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-01-08
 */
public interface ILocationService extends IService<Location> {

    /**
     * 地址逆编译
     * */
    Result<Object> geocodeLocationService(Location location);

    /**
     * 地址打卡
     * */
    Result<String> punchLocationService(Location location);

    /**
     * 分页查询地址打卡列表
     * */
    Result<Page<Location>> punchLocationListService(Page<Location> page);

    /**
     * 删除打卡日记
     * */
    Result<String> punchLocationDeleteService(Location location);

    /**
     * 搜索当天地址打卡
     * */
    Result<List<Location>> getLocationListService(Date date);
}
