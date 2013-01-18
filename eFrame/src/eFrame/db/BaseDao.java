package eFrame.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eFrame.annotations.db.Column;
import eFrame.annotations.db.ColumnType;
import eFrame.annotations.db.TableBean;
import eFrame.exception.DaoException;
import eFrame.utils.DBUtil;
import eFrame.utils.ReflectionUtil;

/**
 * 一些公用数据库增删改查用的方法。
 * <br>
 * 后面增加泛型取得其类型等，做专用的get entity等。
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public abstract class BaseDao<T> {
	
	private Class<?> genericClass = null;
	
	/**
	 * 返回修改行数
	 * @param sql
	 * @return
	 */
	public int update(String sql){
		try {
			return DBUtil.update(sql, null);
		} catch (SQLException e) {
			throw new DaoException(e.toString(),e); 
		}
	}
	
	/**
	 * 修改单个的对象
	 * @param entity
	 * @return
	 */
	public int update(T entity){
		TableBean tb = genericClass.getAnnotation(TableBean.class);
		String tableName = tb.name();
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("update "+tableName+" set ");
		List<Param> paramList = ReflectionUtil.generateEntityValueList(entity, false);
		for(Param p:paramList){
			String fieldName = p.getFieldName();
			sqlBuilder.append(fieldName+"=?,");				
		}
		//去掉最后的一个‘,’
		String sql = sqlBuilder.substring(0, sqlBuilder.length()-1);	
		Param KeyField = ReflectionUtil.getKeyField(entity);
		sql += " where "+KeyField.getFieldName()+"=?";
		try {
			return DBUtil.update(sql, paramList);
		} catch (SQLException e) {
			throw new DaoException(e.toString(),e); 
		}
	}
	
	/**
	 * 删除， id是long类型的
	 * @param id
	 * @return
	 */
	public int deleteById(Long id){
		return _deleteById(id, ColumnType.BigInt);
	}
	
	public T findById(Integer id){
		TableBean tb = genericClass.getAnnotation(TableBean.class);
		String tableName = tb.name();
		
		Field[] fields = genericClass.getDeclaredFields();
		for(Field f:fields){
			//没有被列注解标注的字段，跳过
			if(!f.isAnnotationPresent(Column.class)){
				continue;
			}
			Column column = f.getAnnotation(Column.class);
			//有列注解标注，但不是主键，跳过
			if(!column.isKey()){
				continue;				
			}
			//剩下的就是有列注解且是主键的字段了
			try {
				Constructor<?> c = genericClass.getConstructor();
				@SuppressWarnings("unchecked")
				T entity = (T)c.newInstance();
				
				String fieldName = f.getName();
				String sql = "select "+ReflectionUtil.getEntityFields(entity)+" from "+tableName+" where "+fieldName+"=?";
				Param p = new Param(fieldName, id, ColumnType.Integer);	
				Map<String, Object> map = DBUtil.findById(sql, p);
				
				for(String key:map.keySet()){
					Object value = map.get(key);
					String methodName = ReflectionUtil.generateSetMethodName(key);
					Method method = entity.getClass().getMethod(methodName, value.getClass());
					method.invoke(entity, value);
				}				
				return entity;
			} catch (Exception e) {
				throw new DaoException(e.toString(),e); 
			}
		}
		throw new DaoException("generate deleteById sql exception!"); 		
	}
	
	/**
	 * 删除， id是int类型的
	 * @param id
	 * @return
	 */
	public int deleteById(Integer id){
		return _deleteById(id, ColumnType.Integer);
	}	
	
	/**
	 * 内部调用的deleteById，此方法不限制id的类型。
	 * </br>
	 * 但是外部调用限制id只能是long或者int类型
	 * @param id
	 * @return
	 */
	private int _deleteById(Object id, ColumnType ct){
		TableBean tb = genericClass.getAnnotation(TableBean.class);
		String tableName = tb.name();
		
		Field[] fields = genericClass.getDeclaredFields();
		for(Field f:fields){
			//没有被列注解标注的字段，跳过
			if(!f.isAnnotationPresent(Column.class)){
				continue;
			}
			Column column = f.getAnnotation(Column.class);
			//有列注解标注，但不是主键，跳过
			if(!column.isKey()){
				continue;				
			}
			//剩下的就是有列注解且是主键的字段了
			String fieldName = f.getName();
			String sql = "delete from "+tableName+" where "+fieldName+"=?";
			List<Param> params = new ArrayList<Param>();
			params.add(new Param(fieldName, id, ct));			
			try {
				return DBUtil.delete(sql, params);
			} catch (SQLException e) {
				throw new DaoException(e.toString(),e); 
			}
		}
		throw new DaoException("generate deleteById sql exception!"); 
	}
	
	/**
	 * 删除语句
	 * 生成 delete by id的sql方法
	 * @param entity
	 * @return
	 */
	public int delete(T entity){
		//主键的名字， 值等信息
		Param keyField = ReflectionUtil.getKeyField(entity);
		//要删除的表的表名
		String tableName = ReflectionUtil.getEntityTableName(entity);
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ");
		sql.append(tableName);
		sql.append("where ");
		sql.append(keyField.getFieldName());
		sql.append("=?");
		List<Param> params = new ArrayList<Param>();
		params.add(keyField);
		try {
			return DBUtil.delete(sql.toString(), params);
		} catch (SQLException e) {
			throw new DaoException(e.toString(),e); 
		}
	}	
	
	/**
	 * 插入语句
	 * 生成的sql是不带id的
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unused")
	public int add(T entity){
		List<Param> paramList = ReflectionUtil.generateEntityValueList(entity, false);
		StringBuilder builder = new StringBuilder("insert into ");
		builder.append(ReflectionUtil.getEntityTableName(entity)+"(");
		for(Param p:paramList){
			builder.append(p.getFieldName()+",");
		}
		//去掉最后的一个','
		String sql = builder.substring(0, builder.length()-1);
		sql += ") values(";
		for(Param p:paramList){
			sql += "?,";
		}		
		//去掉最后的一个','		
		sql = sql.substring(0, sql.length()-1);
		sql+=")";
		try {
			return DBUtil.insert(sql, paramList);
		} catch (SQLException e) {
			throw new DaoException(e.toString(),e); 
		}
	}
	
	/**
	 * 返回sql中count语句的结果
	 * @param sqlCount
	 * @return
	 */
	public int getCount(String sqlCount){
		try {
			return DBUtil.getCount(sqlCount);
		}catch(Exception e){
			throw new DaoException(e.toString(),e); 
		}
	}
	
	/**
	 * 取出分页对象
	 * @param sqlQuery
	 * @param sqlCount
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page<T> getPage(String sqlQuery, String sqlCount, int pageNo, int pageSize){
		List<T> list =this.getList(sqlQuery+" limit "+pageSize*(pageNo-1)+","+pageSize);
		int total = this.getCount(sqlCount);
		return new Page<T>(pageSize, total, pageNo, list);
	}
	
	/**
	 * 查询，取出一个List的数据
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<T> getList(String sql){
		List<T> result = new ArrayList<T>();		
		try{
			//db operation
			List<HashMap<String, Object>> list = DBUtil.getList(sql);
			if(list==null||list.size()==0){
				return result;
			}
			
			//reflection map to entity
			for(HashMap<String, Object> m:list){
				Constructor<?> c = genericClass.getConstructor();
				@SuppressWarnings("unchecked")
				T entity = (T)c.newInstance();
				
				for(String key:m.keySet()){
					Object value = m.get(key);
					String methodName = ReflectionUtil.generateSetMethodName(key);
					Method method = entity.getClass().getMethod(methodName, value.getClass());
					method.invoke(entity, value);
				}
				result.add(entity);
			}			
		}catch(Exception e){
			throw new DaoException(e.toString(),e); 
		}
		return result;
	}
	
	/**
	 * 在系统初始化时候， 依赖注入泛型genericClass。
	 * @param genericClass
	 */
	public final void setGenericClass(Class<?> genericClass) {
		this.genericClass = genericClass;
	}	
	
}
