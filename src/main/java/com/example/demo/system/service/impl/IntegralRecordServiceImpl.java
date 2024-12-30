package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.IntegralRecord;
import com.example.demo.system.mapper.IntegralRecordMapper;
import com.example.demo.system.service.IIntegralRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-12-28
 */
@Service
public class IntegralRecordServiceImpl extends ServiceImpl<IntegralRecordMapper, IntegralRecord> implements IIntegralRecordService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IntegralRecordMapper integralRecordMapper;

    @Override
    public Result<IPage<IntegralRecord>> getIntegralRecordService(int current, int size) {
        // 获取用户id
        String userId = hostHolder.getUser().getId();
        Page<IntegralRecord> page = new Page<>(current, size);

        LambdaQueryWrapper<IntegralRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IntegralRecord::getUserId, userId).orderByAsc(IntegralRecord::getCreateTime);
        try {
            IPage<IntegralRecord> list = page(page, queryWrapper);
            return Result.success(list);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    @Override
    public Result<String> saveIntegralRecordService(IntegralRecord integralRecord) {
        // 获取用户id
        String userId = hostHolder.getUser().getId();
        integralRecord.setUserId(userId);
        try {
            save(integralRecord);
            return Result.success("成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    @Override
    public Result<Integer> getMyIntegralService() {
        // 获取用户id
        String userId = hostHolder.getUser().getId();
        try {
            int total = integralRecordMapper.calculateTotal(userId);

            return Result.success(total);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    @Override
    public Result<Integer> todayGetIntegralService() {
        String userId = hostHolder.getUser().getId();

        try {
            int todayCount = integralRecordMapper.getTodayValueSum(userId, 0);
            return Result.success(todayCount);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }
}
