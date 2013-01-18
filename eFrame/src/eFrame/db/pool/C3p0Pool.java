package eFrame.db.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.utils.Configuration;

import eFrame.db.pool.base.IPool;

/**
 * c3p0连接池实现
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class C3p0Pool implements IPool{

	private static Logger log = Logger.getLogger(C3p0Pool.class);
	
	private static ComboPooledDataSource dataSource = new ComboPooledDataSource();
	
	/** 数据库连接池只需要一个，所以单例 */
	private static C3p0Pool instance;
	
	public static C3p0Pool getInstance(){
		if(instance==null){
			instance = new C3p0Pool();
		}
		return instance;
	}
	
	static {  
        try {  
        	//getting db config
        	Map<String,String> m = Configuration.getInstance().getMap("db.properties");
        	//init datasource
            dataSource.setDriverClass(m.get("db.driver"));  
            dataSource.setJdbcUrl(m.get("db.url"));  
            dataSource.setUser(m.get("db.user"));  
            dataSource.setPassword(m.get("db.pass"));  
            dataSource.setInitialPoolSize(Integer.parseInt(m.get("db.pool.minSize")));  
            dataSource.setMinPoolSize(Integer.parseInt(m.get("db.pool.minSize")));  
            dataSource.setMaxPoolSize(Integer.parseInt(m.get("db.pool.maxSize")));  
            dataSource.setIdleConnectionTestPeriod(Integer.parseInt(m.get("db.pool.idleConnectionTestPeriod")));   
        } catch (Exception e) {  
        	log.error(e,e);
        }  
          
          
    }
	
	@Override
	public void destory() {
		dataSource.close();
	}

	@Override
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			log.error(e,e);
		}
		return null;
	}

}
