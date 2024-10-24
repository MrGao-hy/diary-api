package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

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
 * @since 2024-10-22
 */
@Data
@TableName("d_mark_mount")
@ApiModel(value = "MarkMount对象", description = "")
public class MarkMount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("景区id")
    private Long mountId;

    @ApiModelProperty("评语")
    private String comment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("打分")
    private Double mark;

    @ApiModelProperty("用户详情")
    @TableField(exist = false)
    private Users.UserInfoVo userInfo;

    public MarkMount() {
        this.createTime = LocalDateTime.now(ZoneId.systemDefault());
    }
}
