package com.example.demo.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.IntegralRecord;
import com.example.demo.system.service.IIntegralRecordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-12-28
 */
@RestController
@RequestMapping("/integralRecord")
public class IntegralRecordController {

    @Autowired
    private IIntegralRecordService integralRecordService;

    @ApiOperation(value = "获取积分使用记录")
    @GetMapping("/list")
    public Result<IPage<IntegralRecord>> getIntegralRecord(@RequestParam(defaultValue = "1") int current, @RequestParam(defaultValue = "15") int size) {
        return integralRecordService.getIntegralRecordService(current, size);
    }

    @ApiOperation(value = "获取我的积分")
    @GetMapping("/my/total")
    public Result<Integer> getMyIntegral() {
        return integralRecordService.getMyIntegralService();
    }

    @ApiOperation(value = "获取我的积分")
    @GetMapping("/today/count")
    public Result<Integer> todayGetIntegral() {
        return integralRecordService.todayGetIntegralService();
    }
}
