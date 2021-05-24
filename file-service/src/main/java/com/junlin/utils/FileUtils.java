package com.junlin.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FileUtils {
	private static DecimalFormat df = new DecimalFormat("#.00");

	/**
	 * base64转图片
	 * 
	 * @param base64Str
	 * @param filePath  文件存放目录，后面带上 “/”
	 * @param fileName  文件名
	 * @return 返回文件绝对路径
	 */
	public static String base64ToFile(String base64Str, String filePath, String fileName) {
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] b = decoder.decodeBuffer(base64Str);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			File pathFile = new File(filePath);
			// 不存在则创建目录
			if (!pathFile.exists()) {
				boolean flag = pathFile.mkdirs();
				log.info("mkdirs path:{}, result:{}", filePath, flag);
			}
			OutputStream out = new FileOutputStream(new File(filePath + fileName));
			out.write(b);
			out.flush();
			out.close();
			return filePath + fileName;
		} catch (Exception e) {
			log.error("base64Str转文件异常", e);
			return null;
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static String getFileSize(String filePath) {
		long size = new File(filePath).length();
		if (size <= 0)
			return "0";
		final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
		return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}

	/**
	 * 输入流转字节数组
	 * 
	 * @param is
	 * @return
	 */
	public static byte[] toByteArray(InputStream is) {
		byte[] byteArr = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[is.available()];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			byteArr = baos.toByteArray();
			is.close();
			baos.close();
		} catch (IOException e) {
			log.error("读取byte数组异常", e);
		}
		return byteArr;
	}

	/**
	 * 删除文件或文件夹
	 * 
	 * @param filePath
	 */
	public static void delete(String filePath) {
		File file = new File(filePath);
		if (!file.exists())
			return;
		if (file.isFile()) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			if (files != null)
				for (File f : files) {
					delete(f.getAbsolutePath());
				}
			file.delete();
		}
	}

	/**
	 * 格式化文件大小，带单位输出
	 * 
	 * @param size
	 * @return
	 */
	public static String formatSize(Long size) {
		if (size == null)
			return null;
		String sizeStr;
		if (size < 1024) {
			sizeStr = df.format((double) size) + " B";
		} else if (size < 1048576) {
			sizeStr = df.format((double) size / 1024) + " K";
		} else if (size < 1073741824) {
			sizeStr = df.format((double) size / 1048576) + " M";
		} else {
			sizeStr = df.format((double) size / 1073741824) + " G";
		}
		return sizeStr;
	}

	/**
	 * 获取后缀
	 * 
	 * @param str
	 * @return 返回图片后缀
	 */
	public static String getSuffix(String str) {
		return str = str.substring(str.lastIndexOf(".") + 1);
	}

	/**
	 * 获取一个文件的md5值(可处理大文件)
	 * 
	 * @return md5 value
	 */
	public static String getMD5(File file) {
		FileInputStream fileInputStream = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			fileInputStream = new FileInputStream(file);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fileInputStream.read(buffer)) != -1) {
				md5.update(buffer, 0, length);
			}
			return new String(Hex.encodeHex(md5.digest()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
	 * 
	 * @param path 图片的绝对路径
	 * 
	 * @return
	 */
	public static String getBase64Str(String path) {
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(path);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return Base64.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 判断文件编码格式
	 * @param path 绝对路径
	 * @return 编码格式
	 */
	public static String getFileEncodeUTFGBK(String path){
		String enc = Charset.forName("GBK").name();
		File file = new File(path);
		InputStream in;
		try {
			in = new FileInputStream(file);
			byte[] b = new byte[3];
			in.read(b);
			in.close();
			if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
				enc = StandardCharsets.UTF_8.name();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return enc;
	}

	/**
	 * 获取某个目录下面的所有文件，进行递归调用
	 * @param path
	 * @return
	 */
	public static List<String> getAllFileFromPath(String path){
		List<String> result = new ArrayList<>();
		File f = new File(path);
		if (!f.exists())
			return result;
		if (f.isFile()) {
			result.add(path);
			return result;
		}

		if (f.isDirectory()){
			File[] files = f.listFiles();
			for(File i : files){
				if(i.isDirectory())
					_addFilePathToList(i,result);
				if(i.isFile())
					result.add(i.getAbsolutePath());
			}
		}
		return result;
	}

	/**
	 * 递归添加
	 * @param file
	 * @param toList
	 */
	private static void _addFilePathToList(File file,List<String> toList){
		if(file.isDirectory()) {
			File[] subFiles = file.listFiles();
			for(File i : subFiles){
				if(i.isDirectory())
					_addFilePathToList(i,toList);
				if(i.isFile())
					toList.add(i.getAbsolutePath());
			}
		}
		if(file.isFile())
			toList.add(file.getAbsolutePath());
	}

}
