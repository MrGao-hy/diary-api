package com.example.demo.system.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest<T> extends Page<T> {

    @ApiModelProperty("其他类集合")
    private T in;
}
