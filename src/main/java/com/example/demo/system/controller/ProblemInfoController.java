package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.ProblemInfo;
import com.example.demo.system.service.IProblemInfoService;
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
 * @since 2024-04-05
 */
@RestController
@RequestMapping("/problemInfo")
public class ProblemInfoController {
    @Autowired
    private IProblemInfoService problemInfoService;

    @ApiOperation(value = "提交反馈意见")
    @PostMapping("/submit")
    public Result setFeedbackApi(@RequestBody ProblemInfo problemInfo) {
        return problemInfoService.problemFeedbackService(problemInfo);
    }
}
