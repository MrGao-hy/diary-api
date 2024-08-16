package com.example.demo.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest {

    @ApiModelProperty("当前页")
    private Integer current;

    @ApiModelProperty("一页个数")
    private Integer size;
}
