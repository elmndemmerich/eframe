package com.action;

import java.util.List;

import com.entity.User;
import com.service.Service;

import eFrame.annotations.ActionBean;
import eFrame.annotations.ActionMethodType;
import eFrame.annotations.ActionType;
import eFrame.annotations.Wired;
import eFrame.server.action.BaseAction;

@ActionBean(name="user", resultType=ActionType.page)
public class Action extends BaseAction{
	
	@Wired(name="service")
	private Service service;	
	
	@ActionMethodType(template="")
	public Object getUserList(){
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
	
	public Object add(User u){
		return service.add(u);
	}
	
	public void setService(Service service) {
		this.service = service;
	}	
}
