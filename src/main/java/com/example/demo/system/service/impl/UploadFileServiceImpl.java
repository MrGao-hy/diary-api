package com.example.demo.system.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.config.MinioConfig;
import com.example.demo.system.entity.UploadFile;
import com.example.demo.system.mapper.UploadFileMapper;
import com.example.demo.system.service.IUploadFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.Constant;
import com.example.demo.utils.TimeFormatConversionUtil;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.policy.PolicyType;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-10
 */
@Service
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {


    private int maxSize = 10;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UploadFileMapper uploadFileMapper;
    @Autowired
    private TimeFormatConversionUtil timeFormatConversionUtil;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private Constant constant;
    @Autowired
    private MinioConfig minioConfig;

    /**
     * 图片/视频上传
     * */
    @Override
    public Map<String, Object> uploadFileService(MultipartFile file) {
        // 文件新名称
        String fileName = "";
        // 图片存放本地路径
        String urlPath = "";
        // 图片线上访问地址
        String url = "";

        if (file.isEmpty()) {
            Result.fail(40000, "图片为空");
        }

        Long size = file.getSize();
        if (size > maxSize * Constant.MB) {
//            return "超出文件上传大小限制" + maxSize + "MB";
        }
        String fileSize = constant.formatFileSize(size);

        //获取上传文件的主文件名与扩展名
        String originalFilename = file.getOriginalFilename();
        String[] split = originalFilename.split("\\.");
        String ext = split[split.length - 1];
        //根据文件扩展名得到文件类型
        String type = getFileType(ext);

        // 使用时间戳给文件新命名
        long nowTimestamp = System.currentTimeMillis();
        fileName = nowTimestamp + "." + ext;
        LocalDateTime nowTime = timeFormatConversionUtil.timestampToDateFormat(nowTimestamp);

        // 图片存放的地址
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = "E:\\upload\\file\\" + type + "\\";
        urlPath = pre + fileName;

        // 当前用户名称
        String username = hostHolder.getUser().getUserName();


        String filePath = null;
        try {
            MinioClient minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
            //存入bucket不存在则创建，并设置为只读
            if (!minioClient.bucketExists(minioConfig.getBucketName())) {
                minioClient.makeBucket(minioConfig.getBucketName());
                minioClient.setBucketPolicy(minioConfig.getBucketName(), "*.*", PolicyType.READ_ONLY);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 文件存储的目录结构
            filePath = type + "/" + sdf.format(new Date()) + "/" + fileName;
            minioClient.putObject(minioConfig.getBucketName(),filePath,file.getInputStream(),file.getContentType());
            // 图片/视频地址
            url = "/file"+minioConfig.getBucketName()+"/"+filePath;


            UploadFile uploadFile = new UploadFile();
            // 把图片信息保存数据库中
            uploadFile.setFileName(fileName);
            uploadFile.setType(type);
            uploadFile.setSize(fileSize);
            uploadFile.setCreateTime(nowTime);
            uploadFile.setUploader(username);
            uploadFile.setExtension(ext);
            uploadFile.setPrimaryName(file.getOriginalFilename());
            uploadFile.setPath(urlPath);
            uploadFile.setUrl(url);
            uploadFileMapper.insert(uploadFile);

            Map<String, Object> data = new HashMap<>();
            data.put("url", url);
            data.put("id", uploadFile.getId());
            return data;
        } catch (IOException | InvalidEndpointException | InvalidPortException e) {
            log.error(e.getMessage(), e);
        } catch (InvalidArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvalidBucketNameException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoResponseException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (RegionConflictException e) {
            throw new RuntimeException(e);
        } catch (InvalidObjectPrefixException e) {
            throw new RuntimeException(e);
        }
//        return "图片上传失败";
        return null;
    }

    /**
     * 上传的文件删除
     * */
    @Override
    public Result deleteFileService(UploadFile uploadFile) {
        LambdaQueryWrapper<UploadFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UploadFile::getUrl, uploadFile.getUrl());
        UploadFile file = getOne(queryWrapper);
        if (file == null) {
            Result.fail(30003, "文件不存在");
        }

        try {

            // 删除minio图片存储
            String del = delete(file.getUrl());

            // 删除数据库图片数据
            removeById(file.getId());
            return Result.success(del);
        } catch (Exception e) {
            return Result.fail(30003, "删除失败:" + e.getMessage());
        }
    }

    /**
     * 删除minio里的文件
     *
     * @param url 文件名 例：http://127.0.0.1:9000/diary/12.png
     */
    public String delete(String url) {
        String str = minioConfig.getEndpoint() + "/" + minioConfig.getBucketName() + "/";
        String name = url.replace(str,"");
        try {
            MinioClient minioClient = new MinioClient(minioConfig.getEndpoint(), minioConfig.getAccessKey(), minioConfig.getSecretKey());
            minioClient.removeObject(minioConfig.getBucketName(), name);
        } catch (Exception e) {
            return "删除失败" + e.getMessage();
        }
        return "删除成功";
    }

    public ResponseEntity<ByteArrayResource> imageUrlService(String type, String time, String name) {
        try {

            String imageUrl = "http://127.0.0.1:9000/diary/" + type + "/" + time + "/" + name;
            // 使用RestTemplate获取图片字节数据
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            // 将字节数据封装为Resource
            ByteArrayResource resource = new ByteArrayResource(imageBytes);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"a.jpg\"");
            // 注意：这里应该根据实际的图片类型来设置Content-Type
            MediaType mediaType;
            if(type.equals("image")) {
                mediaType = MediaType.IMAGE_JPEG;
            } else {
                mediaType = MediaType.parseMediaType("video/mp4");
            }
            // 返回图片资源
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType) // 或者使用MediaType.parseMediaType(headers.getFirst(HttpHeaders.CONTENT_TYPE))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    /**
     * 根据文件扩展名给文件类型
     *
     * @param extension 文件扩展名
     * @return 文件类型
     */
    private static String getFileType(String extension) {
        String document = "txt doc pdf ppt pps xlsx xls docx csv";
        String music = "mp3 wav wma mpa ram ra aac aif m4a";
        String video = "avi mpg mpe mpeg asf wmv mov qt rm mp4 flv m4v webm ogv ogg";
        String image = "bmp dib pcp dif wmf gif jpg tif eps psd cdr iff tga pcd mpt png jpeg";
        if (image.contains(extension)) {
            return "image";
        } else if (document.contains(extension)) {
            return "document";
        } else if (music.contains(extension)) {
            return "music";
        } else if (video.contains(extension)) {
            return "video";
        } else {
            return "other";
        }
    }

}
