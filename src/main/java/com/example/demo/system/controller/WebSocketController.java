package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.config.MyWebSocketEndpoint;
import com.example.demo.utils.RandomUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@EnableScheduling
@Component
public class WebSocketController {
    //设置定时十秒一次
    @Scheduled(cron = "0/10 * * * * ?")
    public String send() {
        JSONObject jsonObject = new JSONObject();
        int max = 100;
        int min = 1;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (String a : months) {
            jsonObject.put(a, RandomUtil.RandomNum(min, max));
        }
        MyWebSocketEndpoint.sendAllMessage(jsonObject.toString());
        return jsonObject.toString();
    }
}
