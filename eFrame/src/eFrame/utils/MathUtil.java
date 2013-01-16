package eFrame.utils;

public class MathUtil {
	/**
	 * 判定某个字符串是不是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		try{
			Integer.parseInt(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 字符串转化为数字
	 * @param str
	 * @return
	 */
	public static int str2Int(String str){
		try{
			return Integer.parseInt(str);			
		}catch(Exception e){
			throw new RuntimeException(str+" can not be parse into int!!!");
		}

	}
}
