package com.junlin.biz;

import com.junlin.utils.FastdfsHelper;
import com.junlin.utils.FileUtils;
import com.junlin.vo.FastDFSFile;
import com.junlin.vo.UploadFileResp;
import com.junlin.vo.UrlDownload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wujunlin
 * @Date: 2021/6/2 0002
 */
@Service
@Slf4j
public class FastdfsBiz implements FileService{

    @Value("${fastdfs.tracker}")
    String trackerHost;

    @Override
    public List<UploadFileResp> uploadFiles(MultipartFile[] files) throws Exception {

        List<UploadFileResp> result = new ArrayList<>();
        for (MultipartFile multipartFile : files) {

            FastdfsHelper helper = new FastdfsHelper(URI.create(trackerHost));

            UploadFileResp resp = new UploadFileResp();
            resp.setFileName(multipartFile.getOriginalFilename());
            resp.setUrl(helper.upload(new FastDFSFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), FileUtils.getSuffix(multipartFile.getOriginalFilename()))));

            result.add(resp);
        }
        return result;
    }

    /**
     * if ( $request_uri ~* ^.*\?n=([^&]+)$ ) {
     *      add_header Content-Disposition "attachment;filename=$arg_n";
     * }
     * nginx配置：如果url参数拼接n=  会添加下载响应头
     * @param aliyuns
     * @return
     */
    @Override
    public List<String> getDownloadUrl(List<UrlDownload> aliyuns) {
        List<String> result = new ArrayList<>();
        for(UrlDownload url : aliyuns){
            result.add(url.getUrl() + "?n=" + url.getFileName());
        }
        return result;
    }
}
