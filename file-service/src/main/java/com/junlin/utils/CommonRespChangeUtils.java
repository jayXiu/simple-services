package com.junlin.utils;

import org.springframework.stereotype.Component;

/**
 * @ClassName : CommonRespChangeUtils
 * @Description : 通用的model转换器
 * @Author : liangzhiyong
 * @Date: 2020-04-30 14:54
 */
@Component
public class CommonRespChangeUtils {

   /* public UploadFileResp convertFileToUploadFileResp(File file){
        UploadFileResp rs = new UploadFileResp();
        if (ObjectUtil.isNull(file))
            return rs;
        rs.setId(file.getId())
                .setAliyunUrl(file.getAliyunUrl())
                .setFileType(file.getFileType())
                .setFileName(file.getFileName())
                .setFileMd5(file.getFileMd5());
        // 区分大小写,前端根据
        String fileType = "";
        if(StringUtils.isNoneBlank(file.getFileType())){
            fileType = file.getFileType().toLowerCase() ;
        }
        return rs;
    }*/
}
