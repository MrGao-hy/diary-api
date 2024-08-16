package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.ApplyList;
import com.example.demo.system.service.IApplyListService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-05-07
 */
@RestController
@RequestMapping("/apply")
public class ApplyListController {
    @Autowired
    private IApplyListService applyListService;

    @ApiOperation(value = "应用列表")
    @GetMapping("/list")
    public Result applyListApi() {
        return applyListService.getApplyListService();
    }

    @ApiOperation(value = "新增应用")
    @PostMapping("/add")
    public Result addApplyApi(@RequestBody ApplyList applyList) {
        return applyListService.addApplyService(applyList);
    }

    @ApiOperation(value = "更改应用数据")
    @PostMapping("/change")
    public Result changeApplyApi(@RequestBody ApplyList applyList) {
        return applyListService.changeApplyService(applyList);
    }

    @ApiOperation(value = "删除数据")
    @PostMapping("/delete")
    @SuppressWarnings({"unchecked","rawtypes"})
    public Result deleteApplyApi(@RequestBody Map<String,Object> reqMap) {
        List<String> ids = (List<String>) reqMap.get("id");
        return applyListService.deleteApplyService(ids);
    }


}
