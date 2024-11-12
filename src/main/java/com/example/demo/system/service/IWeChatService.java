package com.example.demo.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.Mount;

import java.util.Map;

public interface IWeChatService extends IService<Mount> {
    Result<Map<String, byte[]>> getPosterQrCodeService(String path, String id);
}
