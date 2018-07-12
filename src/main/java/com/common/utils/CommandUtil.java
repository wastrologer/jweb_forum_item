package com.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandUtil {

	static final Logger logger = LoggerFactory.getLogger(CommandUtil.class);
	
	public static void main(String[] args){
		System.out.println(exec(args[0],args[1],args[2]));
	}
	
//	public static boolean exec(String videoFilePath, String imgFilePath, String imgSize) {
//		ProcessBuilder builder = null;
//		try{
//			List<String> cmds = new ArrayList<String>();
//			cmds.add("ffmpeg");
//			cmds.add("-i");
//			cmds.add(videoFilePath);
//			cmds.add("-y");
//			cmds.add("-f");
//			cmds.add("image2");
////			cmds.add("-ss");
////			cmds.add("1");// 1s位置截取
//			cmds.add("-t");
//			cmds.add("0.001");
//			cmds.add("-s");
//			cmds.add(imgSize);
//			cmds.add(imgFilePath);
//			builder = new ProcessBuilder();
//			builder.command(cmds);
//			builder.start();
//			return true;
//		}catch(Exception e){
//			logger.error(e.getMessage(), e);
//			return false;
//		}finally{
//			builder.directory();
//		}
//	}
	
	public static boolean exec(String videoFilePath, String imgFilePath, String imgSize) {
		try{
			String[] cmds = {"ffmpeg", "-i", videoFilePath,"-y","-f","image2",
			                 "-t","0.001","-s",imgSize,imgFilePath};
			Process process = Runtime.getRuntime().exec(cmds);
			process.waitFor();
			InputStream is = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while((line = reader.readLine()) != null){
			}
			reader.close();
			is.close();
			process.destroy();
			return true;
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			return false;
		}
	}
}
