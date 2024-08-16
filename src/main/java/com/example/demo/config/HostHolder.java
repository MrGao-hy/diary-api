package com.example.demo.config;

import com.example.demo.system.entity.Users;
import org.springframework.stereotype.Component;

/**
 * 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {

    //ThreadLocal本质是以线程为key存储元素
    private ThreadLocal<Users> users = new ThreadLocal<>();

    public void setUser(Users user) {
        users.set(user);
    }

    public Users getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}

