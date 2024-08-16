package com.example.demo.system.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.system.service.impl.WebSocketServer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableScheduling
@Component
public class WebSocketController {
    //设置定时十秒一次
    @Scheduled(cron = "0 0 0/2 * * ?")
    public String send() {
        Map<String,Object> map = new HashMap<>();
        //服务器信息
//        Server server = new Server();
//        server.copyTo();
        map.put("server","1111");
        JSONObject jsonObject =  new JSONObject(map);
        String msg = "我说条小学";
        WebSocketServer.sendAllMessage(msg);
        return jsonObject.toString();
    }
}
