package com.example.demo.system.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.vo.Result;
import com.example.demo.config.ClassApiConfig;
import com.example.demo.config.HostHolder;
import com.example.demo.config.HttpClientConfig;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.ClockImage;
import com.example.demo.system.entity.Location;
import com.example.demo.system.entity.UploadFile;
import com.example.demo.system.mapper.ClockImageMapper;
import com.example.demo.system.mapper.LocationMapper;
import com.example.demo.system.mapper.UploadFileMapper;
import com.example.demo.system.service.ILocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-01-08
 */
@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location> implements ILocationService {

    @Autowired
    private ClassApiConfig api;
    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private ClockImageMapper clockImageMapper;
    @Autowired
    private LocationMapper locationMapper;
    @Autowired
    private UploadFileMapper uploadFileMapper;
    @Autowired
    private UploadFileServiceImpl uploadFileService;

    /**
     * 地址逆编译
     * */
    @Override
    public Result<Object> geocodeLocationService(Location location) {
        if (location == null) {
            return Result.fail("没有传参");
        }

        String url = api.getAmap().getLocation().getHost() + api.getAmap().getLocation().getPath().getRegeo() + "?output=json&key=" + api.getAmap().getKey() + "&radius=1000&extensions=all&location=" + location.getLongitude() + "," + location.getLatitude();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String json = HttpClientConfig.sendGetRequest(url, params);
        JSONObject obj = JSONObject.parseObject(json);//转JSONObject对象

        if (!Objects.equals(obj.getString("status"), "1")) {
            return Result.fail("解析地址失败");
        }

        return Result.success(obj.get("regeocode"));
    }

    /**
     * 打卡
     * */
    @Override
    public Result<String> punchLocationService(Location location) {
        // 获取用户id
        String userId = hostHolder.getUser().getId();
        String username = hostHolder.getUser().getUserName();
        location.setUserId(userId);
        location.setAuthor(username);
        // 图片注入中间表
        List<Long> imgList = location.getImageId();

        try {
            // 保存数据
            boolean bool = save(location);
            for (Long id : imgList) {
                ClockImage img = new ClockImage();
                img.setImageId(id);
                img.setClockInId(location.getId());
                clockImageMapper.insert(img);
            }
            if (bool) {
                return Result.success("打卡成功");
            }
            return Result.fail("打卡失败");
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }

    /**
     * 分页查询地址打卡列表
     */
    @Override
    public Result<Page<Location>> punchLocationListService(Page<Location> page) {
        // 获取用户id
        String userId = hostHolder.getUser().getId();

        LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Location::getUserId, userId);
        queryWrapper.orderByDesc(Location::getCreateTime);
        Page<Location> dataS = locationMapper.selectPage(page, queryWrapper);
        for (Location datum : dataS.getRecords()) {
            LambdaQueryWrapper<ClockImage> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ClockImage::getClockInId, datum.getId());
            List<ClockImage> clockImages = clockImageMapper.selectList(wrapper);
            if (CollectionUtils.isEmpty(clockImages)) {
                continue;
            }
            final List<Long> collect = clockImages.stream().map(ClockImage::getImageId).collect(Collectors.toList());
            LambdaQueryWrapper<UploadFile> wrapper1 = Wrappers.lambdaQuery();
            wrapper1.in(UploadFile::getId, collect);
            List<UploadFile> uploadFiles = uploadFileMapper.selectList(wrapper1);
            List<String> collect1 = uploadFiles.stream().map(UploadFile::getUrl).collect(Collectors.toList());
            datum.setImageList(collect1);
        }
        return Result.success(dataS);
    }

    /**
     * 删除打卡日记
     * */
    @Override
    public Result<String> punchLocationDeleteService(Location location) {
        try {
            LambdaQueryWrapper<Location> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Location::getId, location.getId());
            Location record = getOne(wrapper);

            if (record == null) {
                return Result.fail("删除失败: 没有该条数据");
            }

            LambdaQueryWrapper<ClockImage> clockImageWrapper = new LambdaQueryWrapper<>();
            clockImageWrapper.eq(ClockImage::getClockInId, record.getId());
            List<ClockImage> clockImageList = clockImageMapper.selectList(clockImageWrapper);

            // 判断有图片，有图片删除中间表和上传的upload表
            if (!clockImageList.isEmpty()) {
                for (ClockImage item : clockImageList) {
                    LambdaQueryWrapper<UploadFile> imageWrapper = new LambdaQueryWrapper<>();
                    imageWrapper.eq(UploadFile::getId, item.getImageId());
                    UploadFile image = uploadFileMapper.selectOne(imageWrapper);
                    // 删除minio图片数据
                    uploadFileService.delete(image.getUrl());
                    uploadFileMapper.delete(imageWrapper);
                }
                clockImageMapper.delete(clockImageWrapper);
            }

            // 判断如果有视频就删除有关的upload表
            if(!record.getVideo().isEmpty()) {
                // 删除minio图片数据
                uploadFileService.delete(record.getVideo());
                LambdaQueryWrapper<UploadFile> videoWrapper = new LambdaQueryWrapper<>();
                videoWrapper.eq(UploadFile::getUrl, record.getVideo());
                uploadFileMapper.delete(videoWrapper);
            }

            // 删除location数据
            removeById(location.getId());
        } catch (Exception e) {
            return Result.fail("删除失败:" + e.getMessage());
        }

        return Result.success("删除成功");
    }

    /**
     * 搜索当天地址打卡
     * */
    @Override
    public Result<List<Location>> getLocationListService(Date date) {
        String userId = hostHolder.getUser().getId();
        QueryWrapper<Location> queryWrapper = new QueryWrapper<>();

        // 获取后一天时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //往后一天
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date time = calendar.getTime();

        queryWrapper.lambda().eq(Location::getUserId, userId).between(Location::getCreateTime, date, time).orderByAsc(Location::getCreateTime);
        List<Location> list = list(queryWrapper);
        if (list == null) {
            return Result.fail("没有当天的数据");
        }
        return Result.success(list);
    }
}
