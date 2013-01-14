package eFrame.utils;

import java.util.Map;

/**
 * 集合类相关工具类
 * <br>
 * @date 2013-1-10
 * @author LiangRL
 * @alias E.E.
 */
public class CollectionUtil {
	
	/**
	 * 把数据归并到Map<String, String[]> map类型的map
	 * @param map
	 * @param name
	 * @param value
	 */
    public static void mergeValueInMap(Map<String, String[]> map, String name, String value) {
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

    /**
     * 把数据归并到Map<String, String[]> map类型的map
     * @param map
     * @param name
     * @param values
     */
    public static void mergeValueInMap(Map<String, String[]> map, String name, String[] values) {
        for (String value : values) {
            mergeValueInMap(map, name, value);
        }
    } 
    
    public static void mergeWith(Map<String, String[]> data, Map<String, String[]> map) {
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
        	mergeValueInMap(data, entry.getKey(), entry.getValue());
        }
    }

    public static void _mergeWith(Map<String, String[]> data, Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
        	mergeValueInMap(data, entry.getKey(), entry.getValue());
        }
    }    
}
