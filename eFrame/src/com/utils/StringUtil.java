package com.utils;

/**
 * 字符串处理工具类
 * <br>
 * @date 2013-1-18
 * @author LiangRL
 * @alias E.E.
 */
public class StringUtil {
	/**
	 * uri根据‘/’切分为一截一截
	 * @param uri
	 * @return
	 */
	public static String[] splictUri(String uri){
		if(uri==null){
			return null;
		}
		String[] arr = uri.split("/");
		if(arr==null){
			return null;
		}
		return arr;
	}
}
