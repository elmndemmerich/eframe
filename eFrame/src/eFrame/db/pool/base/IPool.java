package eFrame.db.pool.base;

import java.sql.Connection;
/**
 * 连接池基类
 * <br>
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public interface IPool {
	/** 获取connection对象 */
	public Connection getConnection();
	
	/** 销毁连接池 */
	public void destory();
}
