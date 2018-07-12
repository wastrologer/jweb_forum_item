package com.common.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	static final Logger logger = LoggerFactory.getLogger(FileUtil.class);
	
	public static void main(String[] args)throws Exception{
//		FileInputStream in = new FileInputStream("C:\\Users\\Administrator\\Pictures\\access.log");
//		String destFilePath = "D:\\upload\\sdlkfsdf/1001/1435904697609895697.jpg";
//		copyFile(in,destFilePath);
//		String fileName = FileUtil.generalFileName();
//		String materialId = FileUtil.generalMaterialId(1001L, fileName, "jpg");
//		String relativePath = materialId.replaceFirst("_", "/").replace("_", ".");
//		
//		String pathName = FileUtil.buildFilePath("d:/", relativePath);
//		
//		System.out.println(fileName);
//		System.out.println(materialId);
//		System.out.println(relativePath);
//		System.out.println(pathName);
//		String url = "http://systemcover-10002267.video.myqcloud.com/6133fe53823933c19080664818cbe8a9";
//		String localPath = "D:\\upload\\1435904697609895697.jpg";
//		downloadFile(url,localPath);
		
		String remotePath= "2/1436409721648596137.jpg";
		String tmp = remotePath.substring(0, remotePath.indexOf("."));
		String localPath = "d:\\upload\\";
	    String bigLocalPath = localPath + tmp + "_big.jpg";
	    String smallLocalPath = localPath + tmp + "_small.jpg";
	    System.out.println(bigLocalPath);
	    System.out.println(smallLocalPath);
	}
	
	public static void downloadFile(String url, String localPath)throws Exception{
		HttpClient client = null;
		HttpMethodBase method = null;
		try{
			client = new HttpClient();
			method = new GetMethod(url);
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream is = method.getResponseBodyAsStream();
				copyFile(is,localPath);
			} else {
				logger.error("download failure,url:"+url);
			}
			
		}catch(Exception e){
			throw e;
		}finally{
			if(method != null){
				method.releaseConnection();
			}
			client.getHttpConnectionManager().closeIdleConnections(0);
		}
		
	}
	
	public static void copyFile(InputStream in, String destFilePath)throws IOException{
		FileOutputStream os = null;
		try{
			File file = new File(destFilePath);
			File parent = file.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
			os = new FileOutputStream(destFilePath);
			int index = -1;
			byte[] b = new byte[1024];
			while((index = in.read(b)) != -1){
				os.write(b, 0, index);
			}
		}catch(IOException e){
			throw e;
		}finally{
			if(os != null){
				try{
					os.close();
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
			}
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	//生成文件名称,不包含后缀
	public static String generalFileName(){
		return System.currentTimeMillis()+""+RandomStringUtils.randomNumeric(6);
	}
	
	//生成附件Id
	public static String generalMaterialId(Long userId, String uniqueName, String suffix){
		return userId+"_"+uniqueName+"_"+suffix;
	}
	
	// 生成文件绝对路径
	public static String buildFilePath(String basePath, String relativePath){
		return basePath + File.separator + relativePath;		
	}
}
