package com.entity;

import eFrame.annotations.db.Column;
import eFrame.annotations.db.ColumnType;
import eFrame.annotations.db.TableBean;

@TableBean(name="t_user")
public class User {
	
	@Column(isKey=true, fieldType=ColumnType.BigInt)
	private Integer id;
	
	@Column(fieldType=ColumnType.String)
	private String name;
	
	@Column(fieldType=ColumnType.String)
	private String password;
	
	@Column(fieldType=ColumnType.String)
	private String email;
	
	@Column(fieldType=ColumnType.String)
	private String phone;	

	@Column(fieldType=ColumnType.String)
	private String comment;
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
