package org.smart4j.chapter.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.smart4j.chapter.model.Customer;

public class CustomerServiceTest {

	private final CustomerService customerService;
	
	public CustomerServiceTest() {
		customerService = new CustomerService();
	}
	
	@Before
	public void init() {
		//初始化数据库
	}
	
	/*
	 * 获取客户列表
	 */
	@Test
	public void getCustomerList() {
		List<Customer> customerList = customerService.getCustomerList();
		System.out.println(customerList.size());
	}
	
	/*
	 * 获取指定客户
	 */
	@Test
	public void getCustomer() {
		customerService.getCustomer(1);
	}
	
	/*
	 * 创建客户
	 */
	@Test
	public void createCustomer() {
		customerService.createCustomer(new HashMap<String, Object>());
	}
	
	/*
	 * 更新客户
	 */
	@Test
	public void updateCustomer() {
		customerService.updateCustomer(1,new HashMap<String, Object>());
	}
	
	/*
	 * 删除客户
	 */
	@Test
	public void delCustomer() {
		customerService.delCustomer(1);
	}
	
}
