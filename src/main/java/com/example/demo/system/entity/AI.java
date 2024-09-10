package com.example.demo.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AI implements Serializable {

    @ApiModelProperty("图片地址")
    private String url;

    @ApiModelProperty("年龄")
    private Integer age;

}
