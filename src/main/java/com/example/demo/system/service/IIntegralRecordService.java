package com.example.demo.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.IntegralRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-12-28
 */
public interface IIntegralRecordService extends IService<IntegralRecord> {

    /**
     * @deprecated 查询积分记录
     * */
    Result<IPage<IntegralRecord>> getIntegralRecordService(int current, int size);

    /**
     * @deprecated 保存积分记录
     * */
    Result<String> saveIntegralRecordService(IntegralRecord integralRecord);

    /**
     * @deprecated 获取我的总积分
     * */
    Result<Integer> getMyIntegralService();

    /**
     * @deprecated 获取今天获取的积分
     * */
    Result<Integer> todayGetIntegralService();
}
