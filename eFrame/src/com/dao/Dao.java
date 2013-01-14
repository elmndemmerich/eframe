package com.dao;

import java.util.List;

import com.entity.User;

import eFrame.annotations.DaoBean;
import eFrame.db.BaseDao;

/**
 * 
 * <br>
 * @date 2012-12-25
 * @author LiangRL
 * @alias E.E.
 */
@DaoBean(name="dao")
public class Dao extends BaseDao<User>{

	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<User> getUserList(){
		List<User> list = getList("select * from t_user");
		return list;
	}
	
}
