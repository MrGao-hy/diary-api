package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-04-05
 */
@Data
@TableName("d_problem_info")
@ApiModel(value = "ProblemInfo对象", description = "")
public class ProblemInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty("问题类型")
    private String type;

    @ApiModelProperty("详细描述")
    private String problemDescription;

    @ApiModelProperty("联系方式")
    private String contactWay;
}
