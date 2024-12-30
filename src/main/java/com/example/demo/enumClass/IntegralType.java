package com.example.demo.enumClass;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum IntegralType {
    USER(1, "使用积分"),
    GET(0, "获取积分"),

    GET_DIARY_INTEGRAL(2, "写每日日记获取积分"),
    GET_PUNCH_CARD_INTEGRAL(3, "登山打卡获取积分");

    //建议枚举对象的属性也加final
    private final int value;
    @JsonValue
    private final String description;

    //构造器前面省略了private
    IntegralType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonCreator
    public IntegralType getEnumByKey(int key){
        for (IntegralType item : values()) {
            if (item.value == key){
                return item;
            }
        }
        throw new IllegalArgumentException("invalid order type key!");
    }
}
