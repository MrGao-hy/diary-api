package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.UploadFile;
import com.example.demo.system.service.IUploadFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-10
 */
//@Controller
@RestController
@RequestMapping("/file")
public class UploadFileController {

    @Autowired
    private IUploadFileService uploadFileService;

    @GetMapping("/all")
    public Result all() {
        return Result.success("成功");
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestBody MultipartFile file) {
        return uploadFileService.uploadFileService(file);
    }

    @ApiOperation(value = "删除上传的文件")
    @PostMapping("/delete")
    public Result deleteFile(@RequestBody UploadFile url) {
        return uploadFileService.deleteFileService(url);
    }

}
