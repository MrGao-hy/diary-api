package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.DiaryText;
import com.example.demo.system.service.IDiaryTextService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 每日日记内容 前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-26
 */
@RestController
@RequestMapping("/diaryText")
public class DiaryTextController {

    @Autowired
    private IDiaryTextService diaryTextService;

    @ApiOperation(value = "保存签到打卡")
    @PostMapping("/save")
    public Result<String> saveDiary(@RequestBody DiaryText diaryText) {
        return diaryTextService.saveDiaryService(diaryText);
    }

    @ApiOperation(value = "签到详情")
    @PostMapping("/detail")
    public Result<DiaryText> getDiaryDetail(@RequestBody DiaryText diaryText) {
        return diaryTextService.getDiaryDetailService(diaryText);
    }

    @ApiOperation(value = "打卡日期")
    @PostMapping("/month/list")
    public Result<List<LocalDate>> getDiaryList(@RequestBody DiaryText diaryText) {
        return diaryTextService.getDiaryListService(diaryText);
    }
}
