package com.example.demo.system.controller;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.UploadFile;
import com.example.demo.system.service.IUploadFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

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

    @ApiOperation(value = "通过后端服务访问minio图片")
    @GetMapping("/diary/{type}/{time}/{name}")
    public ResponseEntity<ByteArrayResource> imageUrl(@PathVariable("type") String type, @PathVariable("time") String time, @PathVariable("name") String name) {
        return uploadFileService.imageUrlService(type, time, name);
    }

}
