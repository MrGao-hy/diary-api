package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.entity.PageRequest;
import com.example.demo.system.service.IMountService;
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
 * @since 2024-10-19
 */
@RestController
@RequestMapping("/mount")
public class MountController {

    @Autowired
    private IMountService mountService;

    @ApiOperation(value =  "创建名山数据")
    @PostMapping("create")
    public Result<String> createMountApi(@RequestBody Mount mount) {
        return mountService.createMountService(mount);
    }

    @ApiOperation(value =  "查询名山列表")
    @PostMapping("query/list")
    public Result<Page<Mount>> queryMountListApi(@RequestBody Page<Mount> page) {
        return mountService.queryMountListService(page);
    }

    @ApiOperation(value =  "查询名山详情")
    @PostMapping("query/detail")
    public Result<Mount> queryMountDetailApi(@RequestBody Mount mount) {
        return mountService.queryMountDetailService(mount);
    }
}
