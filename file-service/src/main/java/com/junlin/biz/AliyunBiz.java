package com.junlin.biz;

import com.junlin.utils.AliyunOSSClientUtils;
import com.junlin.utils.DateUtils;
import com.junlin.utils.FileUtils;
import com.junlin.vo.UrlDownload;
import com.junlin.vo.UploadFileResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: wujunlin
 * @Date: 2021/5/24 0024
 */
//@Service
@Slf4j
public class AliyunBiz implements FileService{

    @Value("${fileConfig.uploadTempBasePath}")
    private String uploadTempBasePath;

    @Override
    public List<UploadFileResp> uploadFiles(MultipartFile[] files) throws Exception {
        List<UploadFileResp> res = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            res.add(uploadFile(multipartFile));
        }
        return res;
    }

    @Override
    public List<String> getDownloadUrl(List<UrlDownload> aliyuns) {
        return  aliyuns.stream().map(vo->{ return AliyunOSSClientUtils.getDownloadUrl(vo.getUrl(), vo.getFileName()); }).collect(Collectors.toList());
    }

    public UploadFileResp uploadFile(MultipartFile file) throws IOException {
        String fullPath = this.writeFileToLocalServer(file);
        String fileMd5 = FileUtils.getMD5(new java.io.File(fullPath));
        //upload aliyun
        String aliyunUrl = AliyunOSSClientUtils.uploadFile(fullPath);
        UploadFileResp fileEntity = new UploadFileResp();
        String ext = FileUtils.getSuffix(fullPath);
        String sourceName = file.getOriginalFilename();
        fileEntity
                .setUrl(aliyunUrl)
                .setFileType(ext)
                .setFileMd5(fileMd5)
                .setFileName(sourceName);

        return fileEntity;
    }

    public String writeFileToLocalServer(MultipartFile file) throws IOException {
        String saveDirPath = uploadTempBasePath + DateUtils.date2Str(new Date(), DateUtils.DF_yyyyMMDD)+"/";
        // ??????????????????????????????????????????
        java.io.File saveDir = new java.io.File(saveDirPath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        String sourceName = file.getOriginalFilename();
        String ext = FileUtils.getSuffix(sourceName);
        String saveFileName = UUID.randomUUID().toString()+"."+ext;
        // java7???????????????
        // ATOMIC_MOVE ??????????????????
        // COPY_ATTRIBUTES ?????????????????????????????????????????????????????????
        // REPLACE_EXISTING ????????????????????????
        Files.copy(file.getInputStream(), Paths.get(saveDirPath, saveFileName),
                StandardCopyOption.REPLACE_EXISTING);
        String fullPath = saveDirPath+saveFileName;
        return fullPath;
    }
}
