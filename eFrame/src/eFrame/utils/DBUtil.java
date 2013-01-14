package eFrame.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import eFrame.annotations.db.ColumnType;
import eFrame.db.Page;
import eFrame.db.Param;
import eFrame.db.pool.C3p0Pool;

/**
 * 与BaseDao对应。
 * <br>BaseDao只是个抽象父类，而这个工具类用于静态调用
 * 有空再补充prepareStatement预编译的方法。
 * List<Map<String,Object>> 这算是弱类型吧？ 
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class DBUtil {
	
	private static Logger log = Logger.getLogger(DBUtil.class);
	
	/**
	 * 用prepareStatment的删除
	 * 返回删除行数
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int delete(String sql, List<Param> params)throws SQLException{
		C3p0Pool pool = C3p0Pool.getInstance();
		Connection conn = pool.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		if(params!=null){
			int i=1;
			for(Param p:params){
				if(p.getColumnType()==ColumnType.Date){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.Integer){
					statement.setInt(i++, (Integer)p.getValue());		
				}else if(p.getColumnType()==ColumnType.BigInt){
					statement.setLong(i++, (Long)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setTimestamp(i++, (Timestamp)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}
			}			
		}

		try{
			log.info(sql);
			return statement.executeUpdate();
		}finally{
			statement.close();
			conn.close();
		}
	}		
	
	/**
	 * 返回修改行数
	 * @param sql
	 * @return
	 */
	public static int update(String sql, List<Param> params)throws SQLException{
		C3p0Pool pool = C3p0Pool.getInstance();
		Connection conn = pool.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql);
		if(params!=null){
			int i=1;
			for(Param p:params){
				if(p.getColumnType()==ColumnType.Date){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.Integer){
					statement.setInt(i++, (Integer)p.getValue());		
				}else if(p.getColumnType()==ColumnType.BigInt){
					statement.setLong(i++, (Long)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setTimestamp(i++, (Timestamp)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}
			}			
		}		
		try{
			log.info(sql);
			return statement.executeUpdate(sql);
		}finally{
			statement.close();
			conn.close();
		}
	}	
	
	/**
	 * 用于select count语句， 返回数量
	 * @param sqlCount
	 * @return
	 * @throws SQLException
	 */
	public static int getCount(String sqlCount) throws SQLException{
		C3p0Pool pool = C3p0Pool.getInstance();
		Connection conn = pool.getConnection();	
		PreparedStatement statement = conn.prepareStatement(sqlCount);
		try{
			log.info(sqlCount);
			ResultSet rs = statement.executeQuery();
			if(rs!=null && rs.next()){  
	            return Integer.valueOf(rs.getInt(1));//返回第一行第一个记录。  
	        }
			
			return -1;
		}finally{
			statement.close();
			conn.close();
		}		
	}
	
	/**
	 * 预编译的insert语句
	 * @param 
	 * 				sql
	 * @param 
	 * 				params
	 * @return	
	 * 				返回插入语句生成的ID
	 * @throws 
	 * 				SQLException
	 */
	public static int insert(String sql, List<Param> params) throws SQLException{
		C3p0Pool pool = C3p0Pool.getInstance();
		Connection conn = pool.getConnection();
		PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		
		if(params!=null){
			int i=1;
			for(Param p:params){
				if(p.getColumnType()==ColumnType.Date){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setDate(i++, (Date)p.getValue());		
				}else if(p.getColumnType()==ColumnType.Integer){
					statement.setInt(i++, (Integer)p.getValue());		
				}else if(p.getColumnType()==ColumnType.BigInt){
					statement.setLong(i++, (Long)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}else if(p.getColumnType()==ColumnType.DateTime){
					statement.setTimestamp(i++, (Timestamp)p.getValue());		
				}else if(p.getColumnType()==ColumnType.String){
					statement.setString(i++, (String)p.getValue());		
				}
			}			
		}		
		
		try{
			log.info(sql);
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs!=null && rs.next()){  
	            return Integer.valueOf(rs.getInt(1));//返回主键值  
	        }
			return -1;
		}finally{
			statement.close();
			conn.close();
		}
	}	
	
	/**
	 * 取得分页对象
	 * @param sqlQuery
	 * @param sqlCount
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws SQLException
	 */
	public static Page<HashMap<String, Object>> getPage(String sqlQuery, 
			String sqlCount, int pageNo, int pageSize) throws SQLException{
		List<HashMap<String, Object>> list = getList(sqlQuery+" limit "+pageSize*(pageNo-1)+","+pageSize);
		int total = getCount(sqlCount);
		return new Page<HashMap<String, Object>>(pageSize, total, pageNo, list);
	}	
	
	/**
	 * 查询操作， 取出数据集。
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public static List<HashMap<String, Object>> getList(String sql) throws SQLException{
		List<HashMap<String,Object>> result = new ArrayList<HashMap<String, Object>>();
		//数据库操作~
		C3p0Pool pool = C3p0Pool.getInstance();
		Connection conn = pool.getConnection();
		Statement statement = conn.createStatement();
		log.info(sql);
		ResultSet rs = statement.executeQuery(sql);
		try{
			ResultSetMetaData metaData = rs.getMetaData();
			//遍历每一行
			while(rs.next()){
				HashMap<String, Object> m = new HashMap<String, Object>();
				//每一行的meta信息
				for(int i = 0; i<metaData.getColumnCount(); ++i){
					String columnLabel = metaData.getColumnLabel(i+1);
					Object obj = rs.getObject(columnLabel);
					m.put(columnLabel, obj);					
				}
				result.add(m);
			}			
			return result;			
		}finally{
			statement.close();
			conn.close();
		}
	}	
}
