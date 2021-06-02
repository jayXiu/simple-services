package com.junlin.biz;

import com.junlin.vo.UrlDownload;
import com.junlin.vo.UploadFileResp;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: wujunlin
 * @Date: 2021/5/22 0022
 */
public interface FileService {

    List<UploadFileResp> uploadFiles(MultipartFile[] files) throws Exception;

    List<String> getDownloadUrl(List<UrlDownload> aliyuns);

}
