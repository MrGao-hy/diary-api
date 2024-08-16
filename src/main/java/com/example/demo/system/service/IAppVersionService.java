package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.AppVersion;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-08
 */
public interface IAppVersionService extends IService<AppVersion> {

    /**
     * 检查app版本
     * */
    Result queryAppVersionService();
}
