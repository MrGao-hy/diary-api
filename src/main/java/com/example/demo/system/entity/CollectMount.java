package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("d_collect_mount")
@ApiModel(value = "CollectMount对象", description = "")
public class CollectMount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("山峰景点id")
    private Long mountId;
}
