package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.entity.MountainRecord;
import com.example.demo.system.service.IMountainRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-11-12
 */
@RestController
@RequestMapping("/mountainRecord")
public class MountainRecordController {

    @Autowired
    private IMountainRecordService mountainRecordService;

    @ApiOperation(value =  "打卡")
    @PostMapping("punch")
    public Result<String> recordTravelApi(@RequestBody MountainRecord mountainRecord) {
        return mountainRecordService.recordTravelService(mountainRecord);
    }

    @ApiOperation(value =  "我的足迹列表")
    @PostMapping("record/list")
    public Result<Page<Mount>> recordListApi(@RequestBody Page<MountainRecord> page) {
        return mountainRecordService.recordListService(page);
    }

}
