package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.CollectMount;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.entity.MountainRecord;
import com.example.demo.system.service.ICollectMountService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-22
 */
@RestController
@RequestMapping("/collect")
public class CollectMountController {
    @Autowired
    private ICollectMountService collectMountService;

    @ApiOperation(value = "收藏接口")
    @PostMapping("mount")
    public Result<Boolean> collectMountApi(@RequestBody CollectMount collectMount) {
        return collectMountService.collectMountService(collectMount);
    }

    @ApiOperation(value = "个人查询景区是否收藏接口")
    @PostMapping("isNo")
    public Result<Boolean> queryIsCollectApi(@RequestBody CollectMount collectMount) {
        return collectMountService.queryIsCollectService(collectMount);
    }

    @ApiOperation(value = "个人查询收藏景区列表")
    @PostMapping("mount/list")
    public Result<Page<Mount>> queryIsCollectListApi(@RequestBody Page<CollectMount> page) {
        return collectMountService.queryIsCollectListService(page);
    }

}
