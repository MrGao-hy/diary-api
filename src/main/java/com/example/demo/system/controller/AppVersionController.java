package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.service.IAppVersionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-03-08
 */
@RestController
@RequestMapping("/app")
public class AppVersionController {

    @Autowired
    private IAppVersionService appVersionService;

    @ApiOperation(value = "最新版本版本")
    @GetMapping("/version")
    public Result queryAppVersion() {
        return appVersionService.queryAppVersionService();
    }

}
