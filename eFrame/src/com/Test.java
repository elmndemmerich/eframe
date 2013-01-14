package com;

import com.action.Action;
import com.entity.User;

import eFrame.container.EntityContainer;

/**
 * 
 * @author LiangRL
 * @alias E.E.
 */
public class Test {
	/** 扫描这个目录下的文件 */
	final static String resourcePackage = "com";
	
	public static void main(String[] args) throws Exception{
		EntityContainer cc = EntityContainer.getInstance();
		cc.invoke(new String[]{"com.action","com.service","com.dao"});
		Action a = (Action)cc.getBean("action");
		
		User u = new User();
		u.setEmail("123@123.com");
		u.setName("A");
		u.setPassword("111");
		u.setPhone("137");
		u.setComment("aa");
		a.add(u);
	}
}
