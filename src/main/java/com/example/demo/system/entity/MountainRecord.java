package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
 * @since 2024-11-12
 */
@Data
@TableName("d_mountain_record")
@ApiModel(value = "MountainRecord对象", description = "")
public class MountainRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("打卡id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("景区id")
    private Long mountId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    public MountainRecord() {
        this.createTime = LocalDateTime.now(ZoneId.systemDefault());
    }
}
