package com.example.demo.enumClass;

import lombok.Data;
import lombok.Getter;

@Getter
public enum StatusCode {
    //常量对象列表，必须在枚举类首行
    NOT_DATA(40001,"哥哥，您查询的数据不存在"),
    SQL_STATUS_ERROR(50001, "操作数据库错误, 请联系管理员"),
    NOT_FOUND_USER(20001, "用户不存在"),
    REPETITION(20002, "数据重复"),
    PASSWORD_ERR(40002, "密码错误"),
    PARAM_NOT_NULL(20005, "参数不能为空"),
    USER_NOT_LOGIN(40004, "用户未登录")
    ;

    //建议枚举对象的属性也加final
    private final int value;
    private final String description;

    //构造器前面省略了private
    StatusCode(int value, String description) {
        this.value = value;
        this.description = description;
    }

}
