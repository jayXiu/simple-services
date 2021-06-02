package com.junlin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: wujunlin
 * @Date: 2021/6/2
 */
@Data
public class AliyunDownloadVO {

    @ApiModelProperty(value = "文件URL")
    private String url;

    @ApiModelProperty(value = "下载名称")
    private String fileName;

}
