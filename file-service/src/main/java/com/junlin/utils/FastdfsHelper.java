package com.junlin.utils;

import com.junlin.vo.FastDFSFile;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @Author: wujunlin
 * @Date: 2021/6/2 0002
 */
@Slf4j
public class FastdfsHelper {

    public static final String SEPARATOR = "/";

    public static void main(String[] args) {

        URI uri = URI.create("fads:120.78.203.244:22122");

        System.out.println(uri);

    }

    public FastdfsHelper(URI uri) { // Initialize Fast DFS Client configurations

        try {
            // 连接超时的时限，单位为毫秒
            ClientGlobal.setG_connect_timeout(2000);
            // 网络超时的时限，单位为毫秒
            ClientGlobal.setG_network_timeout(30000);
            ClientGlobal.setG_anti_steal_token(false);
            // 字符集
            ClientGlobal.setG_charset("UTF-8");
            ClientGlobal.setG_secret_key(null);
            // HTTP访问服务的端口号
            ClientGlobal.setG_tracker_http_port(7070);
            // Tracker服务器列表
            InetSocketAddress[] tracker_servers = new InetSocketAddress[1];
            tracker_servers[0] = new InetSocketAddress(uri.getHost(), uri.getPort());
            ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
        } catch (Exception e) {
            log.error("init fail", e);
        }
    }

    public String upload(FastDFSFile file) {
        log.info("File Name: " + file.getName() + "		File Length: " + file.getContent().length);
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("width", file.getWidth());
        meta_list[1] = new NameValuePair("heigth", file.getHeight());
        meta_list[2] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        TrackerServer trackerServer = null;
        StorageClient storageClient = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            trackerServer = trackerClient.getConnection();
            StorageServer storageServer = null;
            storageClient = new StorageClient(trackerServer, storageServer);

            // if(ProtoCommon.activeTest(storageServer.getSocket())){
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
            // }
        } catch (IOException e) {
            log.error("IO Exception when uploadind the file: " + file.getName(), e);
        } catch (Exception e) {
            log.error("Non IO Exception when uploadind the file: " + file.getName(), e);
        }
        log.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms");

        if (uploadResults == null) {
            log.error("upload file fail, error code: " + storageClient.getErrorCode());
        }

        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];

        String[] remoteFileNames = remoteFileName.split("\\.");
        log.info("upload file successfully!!!  " + "group_name: " + groupName + ", remoteFileName:" + " "
                + remoteFileName);
        try {
            trackerServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (remoteFileNames[1].equalsIgnoreCase("jpg") || remoteFileNames[1].equalsIgnoreCase("jpeg")
                || remoteFileNames[1].equalsIgnoreCase("gif") || remoteFileNames[1].equalsIgnoreCase("png")
                || remoteFileNames[1].equalsIgnoreCase("bmp")) {
            return SEPARATOR + groupName + SEPARATOR + remoteFileNames[0] + "!" + file.getWidth() + "x"
                    + file.getHeight() + "." + remoteFileNames[1];
        } else {
            return SEPARATOR + groupName + SEPARATOR + remoteFileName;
        }
    }
}
