package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    @ApiModelProperty("评论id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("景区id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long mountId;

    @ApiModelProperty("评语")
    private String comment;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("打分")
    private Double mark;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("用户详情")
    @TableField(exist = false)
    private Users.UserInfoVo userInfo;

    @ApiModelProperty("总回复条数")
    @TableField(exist = false)
    private Long allReply;

    public MarkMount() {
        this.createTime = LocalDateTime.now(ZoneId.systemDefault());
    }
}
