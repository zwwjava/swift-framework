package org.swift.chapter.model;

/**
 * 客户
 * @author 旺旺
 *
 */
public class Customer {
	/*
	 * id 主键
	 */
	private long id;
	/*
	 * 名称
	 */
	private String name;
	/*
	 * 联系人
	 */
	private String contact;
	/*
	 * 联系电话
	 */
	private String telephone;
	/*
	 * 邮箱
	 */
	private String email;
	/*
	 * 备注
	 */
	private String remark;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
