package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.DiaryText;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-26
 */
public interface IDiaryTextService extends IService<DiaryText> {
    /**
     * 保存个人当天日记，重复就更新
     * */
    Result<String> saveDiaryService(DiaryText diaryText);

    /**
     * 获取个人当天日记详情
     * */
    Result<DiaryText> getDiaryDetailService(DiaryText diaryText);

    /**
     * 获取个人当月所有日记时间
     * */
    Result<List<LocalDate>> getDiaryListService(DiaryText diaryText);
}
