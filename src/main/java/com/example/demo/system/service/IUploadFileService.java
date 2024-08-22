package com.example.demo.system.service;

import com.example.demo.common.vo.Result;
import com.example.demo.system.entity.UploadFile;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-10
 */
public interface IUploadFileService extends IService<UploadFile> {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return 上传信息
     */
    Map<String, Object> uploadFileService(MultipartFile file);

    /**
     * 删除上传文件
     * @param url 文件地址
     */
    Result deleteFileService(UploadFile url);

    /**
     * 通过后端服务访问minio图片地址
     * @param type 文件类型
     * @param time 文件上传时间
     * @param name 名称
     */
    ResponseEntity<ByteArrayResource> imageUrlService(String type, String time, String name);
}
