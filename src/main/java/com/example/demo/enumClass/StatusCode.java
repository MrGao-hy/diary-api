package com.example.demo.enumClass;

import lombok.Data;
import lombok.Getter;

@Getter
public enum StatusCode {
    //常量对象列表，必须在枚举类首行
    NOT_FOUND_USER(20001, "用户不存在"),
    REPETITION(20002, "数据重复"),
    PARAM_NOT_NULL(20005, "参数不能为空"),
    NOT_DATA(40001,"哥哥，您查询的数据不存在"),
    PASSWORD_ERR(40002, "密码错误"),
    USER_NOT_LOGIN(40004, "用户未登录"),
    User_Not_Delete(40005, "无法删除别人的数据, 请换个账号试试"),
    SQL_STATUS_ERROR(50001, "操作数据库错误, 请联系管理员")
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
