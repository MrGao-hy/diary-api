package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
 * @since 2024-11-14
 */
@Data
@TableName("d_mark_reply")
@ApiModel(value = "MarkReply对象", description = "")
public class MarkReply implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("回复id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    @ApiModelProperty("评论id")
    private Long markId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("回复内容")
    private String content;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户详情")
    @TableField(exist = false)
    private Users.UserInfoVo userInfo;

    public MarkReply() { this.createTime = LocalDateTime.now(ZoneId.systemDefault()); }
}
