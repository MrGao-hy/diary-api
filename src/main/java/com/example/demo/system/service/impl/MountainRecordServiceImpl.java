package com.example.demo.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HostHolder;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.entity.MountainRecord;
import com.example.demo.system.mapper.MountMapper;
import com.example.demo.system.mapper.MountainRecordMapper;
import com.example.demo.system.service.IMountainRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-11-12
 */
@Component
public class MountainRecordServiceImpl extends ServiceImpl<MountainRecordMapper, MountainRecord> implements IMountainRecordService {

    @Autowired
    private MountMapper mountMapper;
    @Autowired
    private HostHolder hostHolder;

    @Override
    public Result<String> recordTravelService(MountainRecord mountainRecord) {

        String userId = hostHolder.getUser().getId();
        mountainRecord.setUserId(userId);
        LambdaQueryWrapper<Mount> queryMountWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<MountainRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryMountWrapper.eq(Mount::getId, mountainRecord.getMountId());
        queryWrapper.eq(MountainRecord::getUserId, userId);

        try {
            Mount mount = mountMapper.selectOne(queryMountWrapper);
            boolean isScope = isInCircle(mountainRecord.getLongitude().doubleValue(), mountainRecord.getLatitude().doubleValue(), mount.getLongitude().doubleValue(), mount.getLatitude().doubleValue(), "3000");
            if(isScope) {
                Long count = count(queryWrapper);
                if(count.equals(1L)) {
                    update(mountainRecord, queryWrapper);
                } else {
                    save(mountainRecord);
                }

                return Result.success("打卡成功");
            } else {
                return Result.fail("不在打卡范围以内，请到景区附近点打卡");
            }
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), StatusCode.SQL_STATUS_ERROR.getDescription() + e);
        }
    }


    /**
     * 计算两点之间是否在圈内
     * */
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
    public static boolean isInCircle(double lon1,double lat1,double lon2,double lat2, String radius){
        final double EARTH_RADIUS = 6378.137;
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double radLon1 = rad(lon1);
        double radLon2 = rad(lon2);
        double jdDistance = radLat1 - radLat2;
        double wdDistance = radLon1 - radLon2;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(jdDistance / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(wdDistance / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000d) / 10000d;
        distance = distance*1000;//将计算出来的距离千米转为米
        System.out.println("两点之间距离:");
        System.out.println(distance);
        double r = Double.parseDouble(radius);
        //判断一个点是否在圆形区域内
        return !(distance > r);
    }

}
