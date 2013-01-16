package eFrame.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import eFrame.annotations.db.Column;
import eFrame.annotations.db.TableBean;
import eFrame.db.Param;

/**
 * 反射相关工具类
 * <br>
 * @date 2013-1-4
 * @author LiangRL
 * @alias E.E.
 */
public class ReflectionUtil {
	
	/**
	 * 取得entity的主键相关信息
	 * @param entity
	 * @return
	 */
	public static Param getKeyField(Object entity){
		if(entity==null){
			return null;
		}
		Field[] fields = entity.getClass().getDeclaredFields();
		for(Field f: fields){
			//用于取得字段类型
			Column c = f.getAnnotation(Column.class);
			
			if(c.isKey()){
				//字段名
				String fieldName = f.getName();
				//字段对应的get方法方法名 用于取得value
				String getMethodName = generateGetMethodName(fieldName);
				//这个entity中，field的value
				Object value = null;
				try {
					value = entity.getClass().getMethod(getMethodName).invoke(entity);
				} catch (Exception e) {
					continue;
				}
				return new Param(fieldName, value, c.fieldType());				
			}
		}		
		return null;		
	}	
	
	/**
	 * 生成entity实体类的参数-值列表
	 * @param entity
	 * @return
	 */
	public static List<Param> generateEntityValueList(Object entity, boolean withKey){
		if(entity==null){
			return null;
		}
		List<Param> result = new ArrayList<Param>();
		Field[] fields = entity.getClass().getDeclaredFields();
		for(Field f: fields){
			//用于取得字段类型
			Column c = f.getAnnotation(Column.class);
			
			//例如插入语句，主键是不需要的。withKey
			if(!withKey){
				if(c.isKey()){
					continue;
				}
			}
			
			//字段名
			String fieldName = f.getName();
			//字段对应的get方法方法名 用于取得value
			String getMethodName = generateGetMethodName(fieldName);
			//这个entity中，field的value
			Object value = null;
			try {
				value = entity.getClass().getMethod(getMethodName).invoke(entity);
			} catch (Exception e) {
				continue;
			}
			Param p = new Param(fieldName, value, c.fieldType());
			result.add(p);
		}
		return result;
	}
	
	/**
	 * 取出entity的类名
	 * @param entity
	 * @return
	 */
	public static String getEntityTableName(Object entity){
		String entityName = entity.getClass().getCanonicalName();
		if(entity.getClass().isAnnotationPresent(TableBean.class)){
			TableBean tableBean = entity.getClass().getAnnotation(TableBean.class);
			String tableName = tableBean.name();
			return tableName;
		}
		throw new RuntimeException("entity:"+entityName +" MUST own TableBean annotation!");
	}		
	
	/**
	 * 生成对应的get方法的名称
	 * @param fieldName
	 * @return
	 */
	public static String generateGetMethodName(String fieldName){
		return "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	}	
	
	/**
	 * 生成对应的set方法的名称
	 * @param fieldName
	 * @return
	 */
	public static String generateSetMethodName(String fieldName){
		return "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
	}	
	
	
	public static Method getMethod(Object obj, String methodName){
		for(Method m : obj.getClass().getMethods()){
			if(m.getName().equalsIgnoreCase(methodName)){
				return m;
			}
		}
		return null;
	}
}
