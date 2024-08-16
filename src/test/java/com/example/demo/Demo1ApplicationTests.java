package com.example.demo;

import com.example.demo.system.mapper.UsersMapper;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Demo1ApplicationTests {

    @Resource
    private UsersMapper userMapper;

    @Test
    void test() {
        val users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

}
