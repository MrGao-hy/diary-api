package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-05-07
 */
@Data
@TableName("d_apply_list")
@ApiModel(value = "ApplyList对象", description = "")
public class ApplyList implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("是否关闭")
    private Integer isFinish;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("页面路径")
    private String url;

    public Boolean getIsFinish() {
        return isFinish == 1 ? true : false;
    }

    public void setIsFinish(Boolean isFinish) {
        this.isFinish = isFinish ? 1 : 0;
    }
}
