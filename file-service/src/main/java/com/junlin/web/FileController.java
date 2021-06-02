package com.junlin.web;

import com.junlin.biz.FileService;
import com.junlin.vo.AliyunDownloadVO;
import com.junlin.vo.UploadFileResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wujunlin
 * @Date: 2021/5/22 0022
 */
@RestController
@RequestMapping("/file")
@Api(value = "文件上传相关服务")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping(value = "/upload", consumes = "multipart/*", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "批量/单个上传文件", response = UploadFileResp.class)
    public List<UploadFileResp> upload(@ApiParam("文件") @RequestParam("file") MultipartFile[] files)
            throws Exception {
        return fileService.uploadFiles(files);
    }

    @PostMapping("/getDownloadUrl")
    @ResponseBody
    @ApiOperation(value = "转换下载地址指定文件名称")
    public List<String> getDownloadUrl(@RequestBody List<AliyunDownloadVO> aliyuns) {
        return fileService.getDownloadUrl(aliyuns);
    }
}
