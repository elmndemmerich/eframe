package eFrame.server.http.paramParser.base;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import eFrame.server.http.paramParser.ApacheMultipartParser;
import eFrame.server.http.paramParser.TextParser;
import eFrame.server.http.paramParser.UrlEncodedParser;

/**
 * 用于把请求体转化为参数。
 * <br>
 * @date 2013-1-10
 * @author LiangRL
 * @alias E.E.
 */
public abstract class DataParser {

    public static Map<String, DataParser> parsers = new HashMap<String, DataParser>();

    static {
    	/** FORM表单提交，enctype属性 */
        parsers.put("application/x-www-form-urlencoded", new UrlEncodedParser());
        /** 既可以文件上载，又可以文本数据 */
        parsers.put("multipart/form-data", new ApacheMultipartParser());
        parsers.put("application/xml", new TextParser());
        parsers.put("application/json", new TextParser());
    }

    public static void putMapEntry(Map<String, String[]> map, String name, String value) {
        String[] newValues = null;
        String[] oldValues = map.get(name);
        if (oldValues == null) {
            newValues = new String[1];
            newValues[0] = value;
        } else {
            newValues = new String[oldValues.length + 1];
            System.arraycopy(oldValues, 0, newValues, 0, oldValues.length);
            newValues[oldValues.length] = value;
        }
        map.put(name, newValues);
    }
    
    public abstract Map<String, String[]> parse(InputStream is);
}
