package com.junlin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel
@Accessors(chain = true)
public class UploadFileResp {

	@ApiModelProperty(value = "文件名称")
	private String fileName;

	@ApiModelProperty(value = "阿里云保存路径")
	private String url;

	@ApiModelProperty(value = "文件类型（jpg、png...）")
	private String fileType;

	@ApiModelProperty(value = "文件md5值")
	private String fileMd5;
}
