package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author gaoxianhua
 * @since 2023-12-26
 */
@Data
@Accessors(chain = true)
@TableName("d_diary_text")
@ApiModel(value = "DiaryText对象", description = "")
public class DiaryText implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("日记标题")
    private String title;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("计划,规划,任务")
    private String plan;

    @ApiModelProperty("积分")
    private BigDecimal integral;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;

    @ApiModelProperty("内容")
    private String content;
}
