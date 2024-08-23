package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.common.vo.Result;
import com.example.demo.config.ClassApiConfig;
import com.example.demo.config.HttpClientConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author 20369
 */
@RestController
@RequestMapping("/request")
public class requestThirdPartyController {

    @Autowired
    private ClassApiConfig api;


    @ApiOperation(value = "网易云音乐列表")
    @GetMapping("/musicList")
    public Result getMusicList(String sort) throws JsonProcessingException {

        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getMusicList() + "?type=all&sort=" + sort;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String json = HttpClientConfig.sendGetRequest(url, params);
        ObjectMapper objectMapper = new ObjectMapper();
        Map[] array = objectMapper.readValue(json, Map[].class);

        return Result.success(array);
    }

    @ApiOperation(value = "网易云音乐详情")
    @GetMapping("/musicOnce")
    public Result getMusicOnce(String id) {

        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getMusic() + "?type=song&media=netease&id=" + id;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String json = HttpClientConfig.sendGetRequest(url, params);
        JSONObject data = JSONObject.parseObject(json);

        return Result.success(data);
    }

    @ApiOperation(value = "首页轮播图")
    @GetMapping("/swiper")
    public Result getSwiperImage() {
        List<Object> dataList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            JSONObject image1 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getAcgimg() + "?type=json");
            if(!image1.getBoolean("success")) {
                return Result.success(image1.getString("message"));
            }
            dataList.add(image1.getString("url"));
        }
        for (int i = 1; i < 3; i++) {
            JSONObject image2 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getGirl() + "?type=json");
            if(!image2.getBoolean("success")) {
                return Result.success(image2.getString("message"));
            }
            dataList.add(image2.getString("url"));
        }
        return Result.success(dataList);
    }

    @ApiOperation(value = "每日一句段子")
    @GetMapping("/word")
    public Result getInAWord() {
        Map<String, Object> data = new HashMap<>();

        // 随机一句骚话
        JSONObject word1 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getSao() + "?type=json");
        if(!word1.getBoolean("success")) {
            return Result.success(word1.getString("message"));
        }
        data.put("sao", word1.get("data"));

        // 随机一句一言
        JSONObject word2 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getIan() + "?type=json");
        if(!word2.getBoolean("success")) {
            return Result.success(word2.getString("message"));
        }
        data.put("ian", word2.get("data"));

        // 随机一段笑话
        JSONObject word3 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getJoke() + "?type=json");
        if(!word3.getBoolean("success")) {
            return Result.success(word3.getString("message"));
        }
        data.put("joke", word3.get("data"));

        // 随机一句情话
        JSONObject word4 = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getLove() + "?type=json");
        if(!word4.getBoolean("success")) {
            return Result.success(word4.getString("message"));
        }
        data.put("love", word4.get("data"));

        return Result.success(data);
    }

    @ApiOperation(value = "摸鱼日记图片")
    @GetMapping("/calendar")
    public Result getFishCalendar() {
        List<Object> dataList = new ArrayList<>();
        // 摸鱼人日历
        JSONObject fish = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getMoyu() + "?type=json");
        dataList.add(fish);

        // 摸鱼人日历
        JSONObject laborer = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getZhichang() + "?type=json");
        dataList.add(laborer);

        return Result.success(dataList);
    }

    @ApiOperation(value = "获取当前天气")
    @GetMapping("/weather")
    public Result getWeatherApi(@RequestParam String city) {
        JSONObject data = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getWeather() + "?city=" + city);
        return Result.success(data);
    }

    @ApiOperation(value = "获取美女海报")
    @GetMapping("/poster/bg")
    public Result getMobileGirl() {
        JSONObject data = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getMobileGirl() + "?type=json");
        return Result.success(data);
    }

    @ApiOperation(value = "获取二维码")
    @GetMapping("/qrcode")
    public Result createQrcodeApi(@RequestParam String content) {
        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getQrcode() + "?text=" + content;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String  inputStream = HttpClientConfig.sendGetRequest(url, params);

        try {
//            String base64Image = convertBytesToBase64(inputStream);
            return Result.success("二维码生成成功",inputStream);
        } catch (Exception e) {
            return Result.fail(30003, "转化失败:" + e.getMessage());
        }
    }

    @ApiOperation(value = "每日励志英语")
    @GetMapping("/english")
    public Result dailyEnglishApi() {
        JSONObject data = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getDailyEnglish());
        if(data.getBoolean("success")) {
            return Result.success(data.get("data"));
        } else {
            return Result.fail(50000, "第三方接口问题");
        }
    }

    @ApiOperation(value = "中英翻译")
    @GetMapping("/translate")
    public Result translateApi(@RequestParam String content) {
        JSONObject data = getWord(api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getTranslate() + "?text=" + content);
        return Result.success(data);
    }

    @ApiOperation(value = "ai小姐姐聊天")
    @GetMapping("/ai")
    public Result openAiApi(@RequestParam String content) {
        JSONObject data = getWord("https://api.linhun.vip/api/fflt?type=json&apiKey=0f6416363e85cdad31122c3170a44d59&name=" + content);

        if (data.getInteger("code").equals(200)) {
            return Result.success(data.get("mub"));
        } else {
            return Result.fail(30001, data.get("msg").toString());
        }
    }

    @ApiOperation(value = "懒洋洋视频")
    @GetMapping("/cover")
    public Result coverSongApi() {
        Map<String, Object> data = new HashMap<>();
        // 懒洋洋翻唱
        String url = "http://api.treason.cn/API/nan.php";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String videoUrl = HttpClientConfig.sendGetRequest(url, params);
        data.put("videoUrl", videoUrl);

        return Result.success(data);
    }

    @ApiOperation(value = "获取当天星座")
    @GetMapping("/constellation")
    public Result getConstellationInfo(String type, String date) {
        // 星座运势
        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getHoroscope() + "?time=" + date + "&type=" + type;
        JSONObject constellation = getWord(url);
        if (constellation.getBoolean("success")) {
            return Result.success(constellation.get("data"));
        }

        return Result.fail(20001, constellation.getString("message"));

    }

    @ApiOperation(value = "抖音视频排行榜")
    @GetMapping("/tiktok")
    public Result getTikTokList(String type) {

        String url = "https://apis.juhe.cn/fapig/douyin/billboard?key=7002fe9fc4bb5e129204f87d92c2f395&size=50&offset=1&type=" + type;
        JSONObject video = getWord(url);

        if (video.getInteger("error_code") == 0) {
            return Result.success(video.get("result"));
        }
        return Result.fail(video.getString("reason"));
    }

    @ApiOperation(value = "抖音热榜")
    @GetMapping("/tiktok/host")
    public Result tiktokHotVideo() {
        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getTiktokHot();
        JSONObject data = getWord(url);

        if (data.getBoolean("success")) {
            return Result.success(data.get("data"));
        }
        return Result.fail(data.getString("message"));
    }

    @ApiOperation(value = "抖音关键字搜索")
    @GetMapping("/tiktok/keyword")
    public Result searchKeywordVideo(String keyword) {
        String url = "https://api.pearktrue.cn/api/dy/keylead/?keyword=" + keyword;
        JSONObject video = getWord(url);

        if (video.getInteger("code") == 200) {
            return Result.success(video.getJSONArray("data"));
        }
        return Result.fail(video.getString("msg"));
    }

    @ApiOperation(value = "抖音搜索")
    @GetMapping("/tiktok/search")
    public Result searchTikTokVideo(String value, Integer y, Integer n) {

        String url = "https://api.linhun.vip/api/ssdouyin?&apiKey=d98a1920fc89eced1ad4641c9ae08fc7&msg=" + value + "&y=" + y + "&n=" + n;
        JSONObject video = getWord(url);

        if (video.getInteger("code").equals(200)) {
            Map<Object, String> data = new HashMap<>();
            data.put("title", video.getString("标题"));
            data.put("author", video.getString("作者"));
            data.put("cover", video.getString("封面"));
            data.put("url", video.getString("视频"));
            return Result.success(data, video.getString("msg"));
        }
        return Result.fail(video.getString("msg"));
    }

    @ApiOperation(value = "漂亮小姐姐跳舞视频")
    @GetMapping("/beautiful/video")
    public Result getBeautifulWomanApi() {
        String url = "https://tucdn.wpon.cn/api-girl/index.php?wpon=json";
        JSONObject video = getWord(url);
        return Result.success(video.getString("mp4"), "请求成功");
    }

    @ApiOperation(value = "解析抖音视频")
    @GetMapping("/tiktok/download")
    public Result analysisDouYinVideo(@RequestParam  String url) {
        String video = "https://api.pearktrue.cn/api/video/douyin?url=" + url;
        JSONObject data = getWord(video);
        if (data.get("code").equals(200)) {
            JSONObject dat = data.getJSONObject("data");
            return Result.success(dat, "视频解析成功");
        }

        return Result.fail(20001, data.getString("msg"));
    }

    /**
     * get请求封装，需要接口返回对象
     *
     * @returns Object 返回对象格式
     */
    public JSONObject getWord(String url) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String json = HttpClientConfig.sendGetRequest(url, params);
        JSONObject obj = JSONObject.parseObject(json);//转JSONObject对象

        return obj;
    }

}
