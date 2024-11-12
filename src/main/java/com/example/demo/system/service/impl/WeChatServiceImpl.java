package com.example.demo.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.common.vo.Result;
import com.example.demo.config.HttpClientConfig;
import com.example.demo.enumClass.StatusCode;
import com.example.demo.system.entity.Mount;
import com.example.demo.system.mapper.MountMapper;
import com.example.demo.system.service.IWeChatService;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class WeChatServiceImpl extends ServiceImpl<MountMapper, Mount> implements IWeChatService {
    // 小程序的AppID和AppSecret
    private static final String APP_ID = "wxd0a9a6016e343f99";
    private static final String APP_SECRET = "861ddc96d5bd624d3b218bc6ba520938";
    // 获取access_token的URL
    private static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APP_ID + "&secret=" + APP_SECRET;

    // 生成小程序码的URL
    private static final String QR_CODE_URL = "https://api.weixin.qq.com/wxa/getwxacode";


    // 获取access_token的方法
    private static String getAccessToken() {

        JSONObject res = getWord(TOKEN_URL);

//        accessToken
        if(res.getInteger("expires_in").equals(7200)){
            return res.getString("access_token");
        }

        return res.getString("errmsg");
    }

    // 生成小程序码的方法
    @Override
    public Result<Map<String, byte[]>> getPosterQrCodeService(String path, String id) {
        String accessToken = getAccessToken();

        StringBuilder strUrl = new StringBuilder();
        strUrl.append(QR_CODE_URL)
                .append("?access_token=")
                .append(accessToken);

        // 封装请求参数到body
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("path", path);
        jsonObject.put("scene", id);
        jsonObject.put("check_path", false);
        jsonObject.put("env_version", "release");

        Map<String, byte[]> data = new HashMap<>();
        LambdaQueryWrapper<Mount> queryWrapper = new LambdaQueryWrapper<>();
        LambdaUpdateWrapper<Mount> updateWrapper = new LambdaUpdateWrapper<>();



        try {
            queryWrapper.eq(Mount::getId, id);
            Mount oneMount = getOne(queryWrapper);
            if(!oneMount.getQrCode().isEmpty()) {
                data.put("base64", Base64.getDecoder().decode(oneMount.getQrCode()));
                return Result.success(data);
            }


            byte[] image = HttpClientConfig.sendPostReturnByte(strUrl.toString(), jsonObject.toJSONString());
            data.put("base64", image);

            String base64Image = Base64.getEncoder().encodeToString(image);
            updateWrapper.eq(Mount::getId, id);
            updateWrapper.set(Mount::getQrCode, base64Image);
            update(updateWrapper);

            return Result.success(data);
        } catch (Exception e) {
            return Result.fail(StatusCode.SQL_STATUS_ERROR.getValue(), "【微信小程序获取小程序码】：获取小程序码异常：" + e.getMessage());
        }
    }

    /**
     * get请求封装，需要接口返回对象
     *
     * @return Object 返回对象格式
     */
    public static JSONObject getWord(String url) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String json = HttpClientConfig.sendGetRequest(url, params);
        JSONObject obj = JSONObject.parseObject(json);//转JSONObject对象

        return obj;
    }
}
