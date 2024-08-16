package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Location;
import com.example.demo.system.service.ILocationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-01-08
 */
@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired
    private ILocationService locationService;

    @ApiOperation(value = "逆编码")
    @PostMapping("geocode")
    public Result geocodeLocationApi(@RequestBody Location location) {
        return locationService.geocodeLocationService(location);
    }

    @ApiOperation(value = "地点打卡")
    @PostMapping("punch")
    public Result punchLocationApi(@RequestBody Location location) {
        return locationService.punchLocationService(location);
    }

    @ApiOperation(value = "分页查询地点打卡列表")
    @GetMapping("punch/list")
    public Result<Page<Location>> punchLocationListApi(@RequestParam int current, int size) {
        return locationService.punchLocationListService(new Page<Location>(current, size));
    }

    @ApiOperation(value = "删除地点打卡日志")
    @PostMapping("punch/delete")
    public Result punchLocationDeleteApi(@RequestBody Location location) {
        return locationService.punchLocationDeleteService(location);
    }

    @GetMapping("list")
    public Result getLocationListApi(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return locationService.getLocationListService(date);
    }
}
