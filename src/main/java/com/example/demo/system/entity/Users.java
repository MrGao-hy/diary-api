package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.api.client.util.DateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-11-18
 */

@Data
@Accessors(chain = true)
@TableName("d_users")
@ApiModel(value = "Users对象", description = "用户信息表")
public class
Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("身份证号")
    private String idCard;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("邮箱")
    private String emit;

    @ApiModelProperty("盐值")
    private String salt;

    @ApiModelProperty("签名")
    private String signature;

    @ApiModelProperty("出生日期")
    private String birthDate;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("院校")
    private String school;

    @ApiModelProperty("学历")
    private String education;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("星座")
    private String constellation;

    @ApiModelProperty("住址")
    private String address;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("权限列表")
    @TableField(exist = false)
    private List<Role> roles;

    @ApiModelProperty("旧密码")
    @TableField(exist = false)
    private String oldPassword;
}
