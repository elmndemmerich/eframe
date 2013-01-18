package com.service;

import java.util.List;

import com.dao.Dao;
import com.entity.User;

import eFrame.annotations.ServiceBean;
import eFrame.annotations.Wired;

/**
 * 
 * <br>
 * @date 2012-12-25
 * @author LiangRL
 * @alias E.E.
 */
@ServiceBean(name="service")
public class Service {
	
	@Wired(name="dao")
	private Dao dao;

	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception 
	 */
	public List<User> getUserList() throws Exception{
		return dao.getUserList();
	}
	
	public int add(User u){
		return dao.add(u);
	}
	
	public User getUser(int id){
		return dao.findById(id);
	}
	
	public void setDao(Dao dao) {
		this.dao = dao;
	}	
}
