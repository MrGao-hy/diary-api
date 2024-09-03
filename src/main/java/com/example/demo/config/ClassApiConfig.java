package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yanyu
 * @version 1.0
 * @description
 * @date 2023/11/21 12:30
 */

@Data
@Component
@ConfigurationProperties("http")
public class ClassApiConfig {
    private Amap amap;
    private HxhApi hxhApi;
    private NetEaseCloudMusicApi netEaseCloudMusicApi;

    @Data
    public static class Amap {
        private Location location;
        private String key;
    }

    @Data
    public static class HxhApi {
        private Location location;
    }

    @Data
    public static class NetEaseCloudMusicApi {
        private Location location;
    }

    @Data
    public static class Location {
        private String host;
        private String port;
        private Path path;
    }

    @Data
    public static class Path {
        private String regeo;
        private String acgimg;
        private String girl;
        private String sao;
        private String ian;
        private String joke;
        private String love;
        private String moyu;
        private String zhichang;
        private String horoscope;
        private String weather;
        private String qrcode;
        private String translate;
        private String tiktokHot;
        private String mobileGirl;
        private String dailyEnglish;

        private String musicList;
        private String classify;
        private String hot;
        private String music;
        private String search;
        private String searchHot;
        private String lyric;
        private String list;
        private String detail;
        private String mp3;
        private String canPlay;
    }

}

