package com.action;

import java.util.List;

import com.entity.User;
import com.service.Service;

import eFrame.annotations.ActionBean;
import eFrame.annotations.Wired;
import eFrame.server.action.BaseAction;

@ActionBean(name="action")
public class Action extends BaseAction{
	
	@Wired(name="service")
	private Service service;	
	
	public String getUserList(){
		try {
			List<User> list = service.getUserList();
			for(User user:list){
				System.out.println(user.getId()+" || " +
							user.getName()+" || " +
							user.getPassword()+" || "+
							user.getPhone()+" || "+
							user.getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int add(User u){
		return service.add(u);
	}
	
	public void setService(Service service) {
		this.service = service;
	}	
}
