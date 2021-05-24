package com.junlin.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.Date;

@Component
public class AliyunOSSClientUtils {
	private static Logger log = LoggerFactory.getLogger(AliyunOSSClientUtils.class);

	private static String ENDPOINT_URL;
	private static String ACCESS_KEY_ID;
	private static String ACCESS_KEY_SECRET;
	private static String BACKET_NAME;
	private static String FOLDER;
	private static String CDN_URL;
	private static String EXTRANETDOMAIN;

	/**
	 * 静态成员赋值 @Value注入
	 */
	@Value("${aliyunOSS.endpointUrl}")
	public void setEndpointUrl(String endpointUrl) {
		ENDPOINT_URL = endpointUrl;
	}
	@Value("${aliyunOSS.accessKeyId}")
	public void setAccessKeyId(String accessKeyId) {
		ACCESS_KEY_ID = accessKeyId;
	}
	@Value("${aliyunOSS.accessKeySecret}")
	public void setAccessKeySecret(String accessKeySecret) {
		ACCESS_KEY_SECRET = accessKeySecret;
	}
	@Value("${aliyunOSS.backetName}")
	public void setBacketName(String backetName) {
		BACKET_NAME = backetName;
	}
	@Value("${aliyunOSS.folder}")
	public void setFOLDER(String folder) {
		FOLDER = folder;
	}
	@Value("${aliyunOSS.cdnUrl}")
	public void setCdnUrl(String cdnUrl) {
		CDN_URL = cdnUrl;
	}
	@Value("${aliyunOSS.extranetDomain}")
	public void setExtranetDomain(String extranetDomain) {
		EXTRANETDOMAIN = extranetDomain;
	}
	
	/**
	 * 获取阿里云OSS客户端对象
	 * 
	 * @return ossClient
	 */
	public static OSSClient getOSSClient() {
		return new OSSClient(ENDPOINT_URL, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
	}

	/**
	 * 创建存储空间
	 * 
	 * @param ossClient
	 *            OSS连接
	 * @param bucketName
	 *            存储空间
	 * @return
	 */
	public static String createBucketName(OSSClient ossClient, String bucketName) {
		// 存储空间
		final String bucketNames = bucketName;
		if (!ossClient.doesBucketExist(bucketName)) {
			// 创建存储空间
			Bucket bucket = ossClient.createBucket(bucketName);
			// logger.info("创建存储空间成功");
			return bucket.getName();
		}
		return bucketNames;
	}

	/**
	 * 删除存储空间buckName
	 * 
	 * @param ossClient
	 *            oss对象
	 * @param bucketName
	 *            存储空间
	 */
	public static void deleteBucket(OSSClient ossClient, String bucketName) {
		ossClient.deleteBucket(bucketName);
		// logger.info("删除" + bucketName + "Bucket成功");
	}

	/**
	 * 创建模拟文件夹
	 * 
	 * @param ossClient
	 *            oss连接
	 * @param bucketName
	 *            存储空间
	 * @param folder
	 *            模拟文件夹名如"qj_nanjing/"
	 * @return 文件夹名
	 */
	public static String createFolder(OSSClient ossClient, String bucketName, String folder) {
		// 文件夹名
		final String keySuffixWithSlash = folder;
		// 判断文件夹是否存在，不存在则创建
		if (!ossClient.doesObjectExist(bucketName, keySuffixWithSlash)) {
			// 创建文件夹
			ossClient.putObject(bucketName, keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
			// logger.info("创建文件夹成功");
			// 得到文件夹名
			OSSObject object = ossClient.getObject(bucketName, keySuffixWithSlash);
			String fileDir = object.getKey();
			return fileDir;
		}
		return keySuffixWithSlash;
	}

	/**
	 * 根据key删除OSS服务器上的文件
	 * 
	 * @param ossClient
	 *            oss连接
	 * @param bucketName
	 *            存储空间
	 * @param folder
	 *            模拟文件夹名 如"qj_nanjing/"
	 * @param key
	 *            Bucket下的文件的路径名+文件名 如："upload/cake.jpg"
	 */
	public static void deleteFile(OSSClient ossClient, String bucketName, String folder, String key) {
		ossClient.deleteObject(bucketName, folder + key);
		// logger.info("删除" + bucketName + "下的文件" + folder + key + "成功");
	}

	/**
	 * 上传图片至OSS
	 * 
	 * @param ossClient
	 *            oss连接
	 * @param file
	 *            上传文件（文件全路径如：D:\\image\\cake.jpg）
	 * @param bucketName
	 *            存储空间
	 * @param folder
	 *            模拟文件夹名 如"qj_nanjing/"
	 * @return String 返回的唯一MD5数字签名
	 */
	public static String uploadObject2OSS(OSSClient ossClient, File file, String filesave, String bucketName,
			String folder) {
		String resultStr = null;
		try {
			// 以输入流的形式上传文件
			InputStream is = new FileInputStream(file);
			// 文件名
			String fileName = filesave;
			// 文件大小
			Long fileSize = file.length();
			// 创建上传Object的Metadata
			ObjectMetadata metadata = new ObjectMetadata();
			// 上传的文件的长度
			metadata.setContentLength(is.available());
			// 指定该Object被下载时的网页的缓存行为
			metadata.setCacheControl("no-cache");
			// 指定该Object下设置Header
			metadata.setHeader("Pragma", "no-cache");
			// 指定该Object被下载时的内容编码格式
			metadata.setContentEncoding("utf-8");
			// 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
			// 如果没有扩展名则填默认值application/octet-stream
			metadata.setContentType(getContentType(fileName));
			// 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
			metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
			// 上传文件 (上传文件流的形式)
			PutObjectResult putResult = ossClient.putObject(bucketName, folder + fileName, is, metadata);
			// 解析结果
			resultStr = putResult.getETag();
		} catch (Exception e) {
			e.printStackTrace();
			// logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
		}
		return resultStr;
	}

	/**
	 * 上传文件
	 * @param filePath 文件路径
	 * @return 文件访问url
	 */
	public static String uploadFile(String filePath){
		log.info("=========开始上传文件到阿里云========");
		String fileUrl = null;
		try {
			OSSClient ossClient = AliyunOSSClientUtils.getOSSClient();
			File file = new File(filePath);
			String fileName = file.getName();
			String result = uploadObject2OSS(ossClient, file, fileName, BACKET_NAME, FOLDER);
			log.info("文件上传到阿里云成功,获取的key为：{}", result);
			fileUrl = CDN_URL + fileName;
		} catch (Exception e) {
			log.error("文件上传到阿里云失败!", e);
		}
		return fileUrl;
	}

	/**
	 * 通过文件名判断并获取OSS服务文件上传时文件的contentType
	 *
	 * @param fileName 文件名
	 * @return 文件的contentType
	 */
	public static String getContentType(String fileName) {
		// 文件的后缀名
		String fileExtension = fileName.substring(fileName.lastIndexOf("."));
		if (".bmp".equalsIgnoreCase(fileExtension)) {
			return "image/bmp";
		}
		if (".gif".equalsIgnoreCase(fileExtension)) {
			return "image/gif";
		}
		if (".jpeg".equalsIgnoreCase(fileExtension) || ".jpg".equalsIgnoreCase(fileExtension)
				|| ".png".equalsIgnoreCase(fileExtension)) {
			return "image/jpeg";
		}
		if (".html".equalsIgnoreCase(fileExtension)) {
			return "text/html";
		}
		if (".txt".equalsIgnoreCase(fileExtension)) {
			return "text/plain";
		}
		if (".vsd".equalsIgnoreCase(fileExtension)) {
			return "application/vnd.visio";
		}
		if (".ppt".equalsIgnoreCase(fileExtension) || ".pptx".equalsIgnoreCase(fileExtension)) {
			return "application/vnd.ms-powerpoint";
		}
		if (".doc".equalsIgnoreCase(fileExtension) || ".docx".equalsIgnoreCase(fileExtension)) {
			return "application/msword";
		}
		if (".pdf".equalsIgnoreCase(fileExtension)) {
			return "application/pdf";
		}
		if (".xlsx".equalsIgnoreCase(fileExtension) || ".xls".equalsIgnoreCase(fileExtension)) {
			return "application/vnd.ms-excel";
		}
		if (".xml".equalsIgnoreCase(fileExtension)) {
			return "text/xml";
		}
		if (".zip".equalsIgnoreCase(fileExtension)) {
			return "application/x-zip-compressed";
		}
		if (".rar".equalsIgnoreCase(fileExtension)) {
			return "application/octet-stream";
		}
		// 默认返回类型
		return "image/jpeg";
	}


	/**
	 * 把阿里云文件下载到本地
	 * @param objectName
	 * @param toFullPath
	 */
	public static void downAsLocalFile(String objectName,String toFullPath){
		// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
		OSSClient ossClient = AliyunOSSClientUtils.getOSSClient();
		ossClient.getObject(new GetObjectRequest(BACKET_NAME, objectName), new File(toFullPath));
		// 关闭OSSClient。
		ossClient.shutdown();
	}


	/**
	 * 获取直接下载的URL
	 */
	public static String getDownloadUrl(String aliyunUrl, String fileName){

		if(StringUtils.isEmpty(aliyunUrl)){
			return "";
		}

		String[] split = aliyunUrl.split("/");
		if(split == null || split.length == 0){
			return "";
		}

		try {
			aliyunUrl = split[split.length - 1];

			String attachment = "?response-content-disposition=attachment";
			if(StringUtils.isNotEmpty(fileName)){
				attachment = "?response-content-disposition=attachment;filename=" + fileName;
			}

			String attachmentEncoder = encoder(attachment);
			aliyunUrl = FOLDER + aliyunUrl + attachment;

			//超时时间
			Date expiration = new Date(new Date().getTime() + 18000 * 1000);
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(BACKET_NAME, aliyunUrl);
			generatePresignedUrlRequest.setExpiration(expiration);

			OSSClient ossClient = getOSSClient();
			ossClient.setEndpoint(EXTRANETDOMAIN);

			return ossClient.generatePresignedUrl(generatePresignedUrlRequest).toString().replace(attachmentEncoder + "?", attachment + "&");
		} catch (Exception e) {
			log.info("阿里云URL转换错误,aliyunUrl:{}",aliyunUrl, e);
		}

		return "";
	}

	private static String encoder(String s) throws UnsupportedEncodingException {
		String encoder = URLEncoder.encode(s, "UTF-8");
		encoder = encoder.replaceAll("\\+", "%20");
		encoder = encoder.replaceAll("\\*", "%2A");
		return encoder;
	}
}