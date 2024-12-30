package com.example.demo.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.IntegralType;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.DiaryText;
import com.example.demo.system.entity.IntegralRecord;
import com.example.demo.system.mapper.DiaryTextMapper;
import com.example.demo.system.service.IDiaryTextService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.system.service.IIntegralRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-26
 */
@Service
public class DiaryTextServiceImpl extends ServiceImpl<DiaryTextMapper, DiaryText> implements IDiaryTextService {

    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private IIntegralRecordService integralRecordService;

    /**
     * 保存个人当天日记，重复就更新
     * */
    @Override
    public Result<String> saveDiaryService(DiaryText diaryText) {
        String userId = hostHolder.getUser().getId();
        diaryText.setUserId(userId);

        LambdaUpdateWrapper<DiaryText> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(DiaryText::getUserId, userId).eq(DiaryText::getCreateDate, diaryText.getCreateDate());

        DiaryText data = getOne(updateWrapper);
        boolean isSave = saveOrUpdate(diaryText, updateWrapper);
        if(data == null) {
            IntegralRecord integralRecord = new IntegralRecord();
            integralRecord.setRemark(IntegralType.GET_DIARY_INTEGRAL);
            integralRecord.setActiveId(diaryText.getId().toString());
            integralRecord.setIntegral(diaryText.getIntegral());
            integralRecord.setType(IntegralType.GET.getValue());
            integralRecordService.saveIntegralRecordService(integralRecord);
        }

        if (!isSave) {
            return Result.fail("保存失败");
        }
        return Result.success("保存成功");
    }

    /**
     * 获取个人当天日记详情
     * */
    @Override
    public Result<DiaryText> getDiaryDetailService(DiaryText diaryText) {
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<DiaryText> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(DiaryText::getUserId, userId).eq(DiaryText::getCreateDate, diaryText.getCreateDate());

        try {
            DiaryText data = getOne(queryWrapper);
            return Result.success(data, "查询成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    /**
     * 获取个人当月所有日记时间
     * */
    @Override
    public Result<List<LocalDate>> getDiaryListService(DiaryText diaryText) {
        String userId = hostHolder.getUser().getId();
        LambdaQueryWrapper<DiaryText> queryWrapper = Wrappers.lambdaQuery();

        LocalDate firstDayOfMonth = diaryText.getCreateDate().with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfMonth = diaryText.getCreateDate().with(TemporalAdjusters.lastDayOfMonth());

        queryWrapper.select(DiaryText::getCreateDate).eq(DiaryText::getUserId, userId).between(DiaryText::getCreateDate, firstDayOfMonth.atStartOfDay(),lastDayOfMonth.atTime(23, 59, 59));

        try {
            List<DiaryText> data = list(queryWrapper);
            List<LocalDate> timrList = new ArrayList<>();
            for (DiaryText datum : data) {
                LocalDate time = datum.getCreateDate();
                timrList.add(time);
            }

            return Result.success(timrList, "查询成功");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }
}
