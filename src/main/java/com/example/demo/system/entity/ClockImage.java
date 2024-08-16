package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-01-20
 */
@Data
@TableName("d_clock_image")
@ApiModel(value = "ClockImage对象", description = "")
public class ClockImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("图片id")
    private Long imageId;

    @ApiModelProperty("打卡图片id")
    private Long clockInId;
}
