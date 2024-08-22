package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.config.MyWebSocketEndpoint;
import com.example.demo.utils.RandomUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@EnableScheduling
@Component
public class WebSocketController {
    //设置定时十秒一次
    @Scheduled(cron = "0 0/10 * * * ?")
    public String send() {
        JSONObject months = new JSONObject();
        ArrayList<Integer> monthVal1 = new ArrayList<>();
        ArrayList<Integer> monthVal2 = new ArrayList<>();
        int max = 12000;
        int min = 4000;
        String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        for (String a : month) {
            monthVal1.add(RandomUtil.RandomNum(min, max));
            monthVal2.add(RandomUtil.RandomNum(min, max));
        }
        months.put("one", monthVal1);
        months.put("two", monthVal2);
        MyWebSocketEndpoint.sendAllMessage(months.toString());
        return months.toString();
    }
}
