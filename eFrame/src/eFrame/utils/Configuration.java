package eFrame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 存放一些公共的变量
 * <br>
 * @important 
 * 		如果有重复的key是不会报错的！
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class Configuration {
	
	public final static String[] properties = 
			new String[]{"context.properties","db.properties","memcached.properties"};
	
	private static Map<String,HashMap<String, String>> m = new HashMap<String, HashMap<String, String>>();
	
	private Configuration() throws Exception{
		for(String p:properties){
			URL url = Thread.currentThread().getContextClassLoader().getResource(p);			
			File f = new File(url.getFile());			
			Properties property = new Properties();
			FileInputStream stream = new FileInputStream(f);
			try{
				property.load(stream);	
				HashMap<String,String> tempMap = new HashMap<String,String>();
				for(Object key:property.keySet()){
					Object value = property.get(key);
					tempMap.put(key.toString(), value.toString());
				}			
				m.put(p, tempMap);
			}finally{
				stream.close();
			}
		}
	}
	
	private static Configuration instance;
	
	public static Configuration getInstance() throws Exception{
		if(instance==null){
			instance = new Configuration();
		}
		return instance;		
	}

	/**
	 * 获取某一类型properties的信息
	 * @param propertyFile
	 * @return
	 */
	public Map<String, String> getMap(String propertyFile){
		return m.get(propertyFile);
	}
	
	/**
	 * 必须指定某个file的某个key
	 * @param key
	 * @return
	 */
	public String get(String propertyFile,String key){
		return m.get(propertyFile).get(key);
	}
	
	public String getDefault(String key, String defaultValue){
		String temp = get(key);
		return temp==null?defaultValue:temp;
	}
	
	/**
	 * 只要指定某个key，但是如果有重复的key，只显示第一个
	 * @param key
	 * @return
	 */
	public String get(String key){
		String result=null;
		for(String baseKey:m.keySet()){
			Map<String,String> subMap = m.get(baseKey);
			if(subMap.containsKey(key)){
				result = subMap.get(key);
				break;
			}
		}
		return result;
	}
}
