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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author gaoxianhua
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private ClassApiConfig api;

    @ApiOperation(value = "热门歌单分类接口")
    @GetMapping("/hot/class")
    public Result getHotClassApi() {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getHot();
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONArray("tags"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "歌单分类接口")
    @GetMapping("/class")
    public Result getAllClassApi() {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getClassify();
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONArray("sub"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "歌单列表接口")
    @GetMapping("/song/list")
    public Result getSongListApi(int id, int page, int size) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getList() + "?id=" + id + "&offset=" + (page - 1) * size + "&limit=" + size;
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONArray("songs"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "判断歌曲能播放吗")
    @GetMapping("/song/can/play")
    public Result judgeSongMp3CanPlayApi(int id) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getCanPlay() + "?id=" + id;
        JSONObject music = getWord(url);

        if (music.getBoolean("success")) {
            return Result.success(music.getBoolean("success"), music.getString("message"));
        }
        return Result.fail(40003, music.getString("message"));
    }

    @ApiOperation(value = "获取歌曲url接口")
    @GetMapping("/song/mp3")
    public Result getSongMp3Api(int id, String level) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getMp3() + "?id=" + id + "&level=" + level;
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONArray("data"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "歌曲详情接口")
    @GetMapping("/song/detail")
    public Result getSongDetailApi(int id) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getDetail() + "?ids=" + id;
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONArray("songs"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "获取歌词接口")
    @GetMapping("/song/lyric")
    public Result getSongLyricApi(int id) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getLyric() + "?id=" + id;
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONObject("lrc").getString("lyric"), "");
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

    @ApiOperation(value = "网易云搜索歌曲接口")
    @GetMapping("/search/song")
    public Result searchMusicList(String keywords) {

        String url = api.getNetEaseCloudMusicApi().getLocation().getHost() + api.getNetEaseCloudMusicApi().getLocation().getPath().getSearch() + "?keywords=" + keywords;
        JSONObject music = getWord(url);

        if (music.getInteger("code") == 200) {
            return Result.success(music.getJSONObject("result").getJSONArray("songs"));
        }
        return Result.fail(50000, "第三方接口请求错误");
    }

//    @ApiOperation(value = "网易云音乐详情")
//    @GetMapping("/song/detail")
//    public Result getMusicOnce(String type) {
//
//        String url = api.getHxhApi().getLocation().getHost() + api.getHxhApi().getLocation().getPath().getMusic() + "/" + type + "?type=json";
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        String json = HttpClientConfig.sendGetRequest(url, params);
//        JSONObject data = JSONObject.parseObject(json).getJSONObject("info");
//
//        return Result.success(data);
//    }

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
