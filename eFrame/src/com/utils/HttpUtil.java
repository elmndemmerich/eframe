package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpUtil {
	
	/**
	 * 
	 * @param stream
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String InputStream2Text(InputStream stream, String encoding) throws IOException{
		StringBuilder content = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(stream, encoding);
		BufferedReader br = new BufferedReader(isr);
		for(;;){
			String temp = br.readLine();
			if(temp!=null){
				content.append(temp);
			}else{
				break;
			}
		}
		return content.toString();			
	}	
}
