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
	public Object getUserList() throws Exception{
		List<User> list = service.getUserList();
		getContext().put("list", list);
		return getContext();
	}

	@ActionMethodType(template="user.vm", resultType=ActionType.page)
	public Object getUser(){
		String id = this.request.getParams().get("id");
		User user = service.getUser(Integer.parseInt(id));
		this.getContext().put("user", user);
		return getContext();
	}	
	
	public Object add(User u){
		return service.add(u);
	}
	
	public void setService(Service service) {
		this.service = service;
	}	
}
