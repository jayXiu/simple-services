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

        List<UploadFileResp> res = new ArrayList<>();
        for (MultipartFile multipartFile : files) {

            FastdfsHelper helper = new FastdfsHelper(URI.create(trackerHost));

            FastDFSFile file = new FastDFSFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), FileUtils.getSuffix(multipartFile.getOriginalFilename()));
            String url = helper.upload(file);

            System.out.println(url);
            //res.add(uploadFile(multipartFile));
        }
        return res;
    }

    @Override
    public List<String> getDownloadUrl(List<UrlDownload> aliyuns) {
        return null;
    }
}
