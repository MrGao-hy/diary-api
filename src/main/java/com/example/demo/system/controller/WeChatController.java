package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.common.vo.Result;
import com.example.demo.system.service.IWeChatService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/wechat")
public class WeChatController {

    @Autowired
    private IWeChatService weChatService;

    @ApiOperation(value = "解析抖音视频")
    @GetMapping("/qrcode")
    public Result<Map<String, byte[]>> analysisDouYinVideo(@RequestParam String path, String id) {
       return weChatService.getPosterQrCodeService(path, id);
    }
}
