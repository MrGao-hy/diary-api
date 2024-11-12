package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.CollectMount;
import com.example.demo.system.entity.MarkMount;
import com.example.demo.system.service.ICollectMountService;
import com.example.demo.system.service.IMarkMountService;
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
@RequestMapping("/mark")
public class MarkMountController {
    @Autowired
    private IMarkMountService markMountService;

    @ApiOperation(value = "景区评价接口")
    @PostMapping("mount")
    public Result<String> markMountApi(@RequestBody MarkMount markMount) {
        return markMountService.markMountService(markMount);
    }

    @ApiOperation(value = "景区下评价列表接口")
    @PostMapping("comment/list")
    public Result<List<MarkMount>> commentListApi(@RequestBody MarkMount markMount) {
        return markMountService.commentListService(markMount);
    }

    @ApiOperation(value = "删除评论接口")
    @PostMapping("comment/delete")
    public Result<Boolean> deleteCommentApi(@RequestBody MarkMount markMount) {
        return markMountService.deleteCommentService(markMount);
    }
}
