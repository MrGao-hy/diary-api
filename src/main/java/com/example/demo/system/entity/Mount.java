package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author gaoxianhua
 * @since 2024-10-19
 */
@Data
@TableName("d_mount")
@ApiModel(value = "Mount对象", description = "")
public class Mount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("山的名称")
    private String name;

    @ApiModelProperty("海拔")
    private Float altitude;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;
}
