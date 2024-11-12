package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.MountainRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-11-12
 */
public interface IMountainRecordService extends IService<MountainRecord> {

    Result<String> recordTravelService(MountainRecord mountainRecord);

    Result<String> recordListService(MountainRecord mountainRecord);
}
