package com.example.demo.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

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
 * @since 2023-12-10
 */
@Data
@Accessors(chain = true)
@TableName("d_upload_file")
@ApiModel(value = "UploadFile对象", description = "上传文件信息表")
public class UploadFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty("文件原名称")
    private String primaryName;

    @ApiModelProperty("文件扩展名")
    private String extension;

    @ApiModelProperty("文件本地存放路径")
    private String path;

    @ApiModelProperty("文件类型")
    private String type;

    @ApiModelProperty("文件大小")
    private String size;

    @ApiModelProperty("上传人")
    private String uploader;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件线上访问地址")
    private String url;
}
