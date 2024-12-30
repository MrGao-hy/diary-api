package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.example.demo.enumClass.IntegralType;
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
 * @since 2024-12-28
 */
@Data
@TableName("d_integral_record")
@ApiModel(value = "IntegralRecord对象", description = "")
public class IntegralRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("绑定活动id")
    private String activeId;

    @ApiModelProperty("积分")
    private int integral;

    @ApiModelProperty("积分类型：1-使用，0-获取")
    private int type;

    @ApiModelProperty("积分获取或者使用备注")
    private IntegralType remark;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    public IntegralRecord() { this.createTime = LocalDateTime.now(ZoneId.systemDefault()); }
}
