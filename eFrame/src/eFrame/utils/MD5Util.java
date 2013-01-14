package eFrame.utils;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class MD5Util {
	
	/**
	 * bytes-->hex bytes-->string
	 * @param bytes
	 * @return
	 */
    public static String byteToHexString(byte[] bytes) {
        return String.valueOf(Hex.encodeHex(bytes));
    }	
	
    /**
     * 字符串转化为16进制的MD5字符串
     * @param value
     * @return
     */
    public static String hexMD5(String value) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(value.getBytes("utf-8"));
            byte[] digest = messageDigest.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
