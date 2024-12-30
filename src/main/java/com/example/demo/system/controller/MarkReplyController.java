package com.example.demo.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.MarkReply;
import com.example.demo.system.entity.PageRequest;
import com.example.demo.system.service.IMarkReplyService;
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
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/markReply")
public class MarkReplyController {

    @Autowired
    private IMarkReplyService markReplyService;

    @ApiOperation(value = "回复评论")
    @PostMapping("/reply")
    public Result<MarkReply> markReplyApi(@RequestBody MarkReply markReply) {

        return markReplyService.markReplyService(markReply);
    }

    @ApiOperation(value = "回复列表")
    @PostMapping("/reply/list")
    public Result<Page<MarkReply>> markReplyListApi(@RequestBody PageRequest<MarkReply> param) {
        return markReplyService.markReplyListService(param);
    }

    @ApiOperation(value = "删除回复")
    @PostMapping("/reply/delete")
    public Result<Boolean> deleteMarkReplyApi(@RequestBody MarkReply markReply) {

        return markReplyService.deleteMarkReplyService(markReply);
    }

}
