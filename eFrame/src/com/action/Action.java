package com.action;

import java.util.List;

import com.entity.User;
import com.service.Service;

import eFrame.annotations.ActionBean;
import eFrame.annotations.ActionMethodType;
import eFrame.annotations.ActionType;
import eFrame.annotations.Wired;
import eFrame.server.action.BaseAction;

@ActionBean(name="user")
public class Action extends BaseAction{
	
	@Wired(name="service")
	private Service service;	
	
	@ActionMethodType(template="userList.vm", resultType=ActionType.page)
	public Object getUserList(){
		try {
			List<User> list = service.getUserList();
			getContext().put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return getContext();
	}
	
	public Object add(User u){
		return service.add(u);
	}
	
	public void setService(Service service) {
		this.service = service;
	}	
}
