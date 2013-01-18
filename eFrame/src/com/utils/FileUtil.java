package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author LiangRL
 * @alias E.E.
 */
public class FileUtil {
	
	private FileUtil(){}
	
	/**
	 * 输入路径，返回文件
	 * @param path
	 * @return
	 */
	public static File getFile(String path){
		return new File(path);
	}

	/**
	 * 遍历文件，此方法只要文件， 不要目录
	 * @param f
	 * @param isRecusive 是否递归遍历
	 * @return
	 */
	public static List<File> getFiles(File f, boolean isRecusive){
		List<File> list = new ArrayList<File>();
		if(f.isFile()){
			return list;
		}
		File[] files = f.listFiles();
		for(File tempFile:files){
			if(tempFile.isFile()){
				list.add(tempFile);
			}else if(isRecusive){	//剩下一定是目录类型。如果递归调用本方法，则递归调用本方法
				list.addAll(getFiles(tempFile,isRecusive));
			}
		}
		return list;
	}	
	
	/**
	 * 是否目录
	 * @param f
	 * @return
	 */
	public static boolean isDirectory(File f){
		if(f.isDirectory()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param f
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static List<String> File2List(File f, String encoding) throws IOException{
		List<String> result = new ArrayList<String>();
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis, encoding);
		BufferedReader br = new BufferedReader(isr);
		try{
			for(;;){
				String temp = br.readLine();
				if(temp!=null){
					result.add(temp);
				}else{
					break;
				}
			}
			return result;			
		}finally{
			br.close();
			isr.close();
			fis.close();
		}
	}	
	
	
	
	/**
	 * 
	 * @param f
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String File2Text(File f, String encoding) throws IOException{
		StringBuilder content = new StringBuilder();
		FileInputStream fis = new FileInputStream(f);
		InputStreamReader isr = new InputStreamReader(fis, encoding);
		BufferedReader br = new BufferedReader(isr);
		try{
			for(;;){
				String temp = br.readLine();
				if(temp!=null){
					content.append(temp);
				}else{
					break;
				}
			}
			return content.toString();			
		}finally{
			br.close();
			isr.close();
			fis.close();
		}
	}
	
}
