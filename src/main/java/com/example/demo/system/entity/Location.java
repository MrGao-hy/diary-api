package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-01-08
 */
@Data
@TableName("d_location")
@ApiModel(value = "Location对象", description = "")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户名称")
    private String author;

    @ApiModelProperty("现在时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("视频地址")
    private String video;

    @ApiModelProperty("语音地址")
    private String audio;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("图片列表")
    @TableField(exist = false)
    private List<Long> imageId;

    @ApiModelProperty("图片列表")
    @TableField(exist = false)
    private List<String> imageList;
}
