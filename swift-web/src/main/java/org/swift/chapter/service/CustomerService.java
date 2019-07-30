package org.swift.chapter.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swift.chapter.model.Customer;
import org.swift.chapter.model.Weather;
import org.swift.framework.annotation.Service;
import org.swift.framework.helper.DataBaseHelper;

/**
 * 客户服务
 * @author 旺旺
 *
 */
@Service
public class CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

	/*
	 * 获取客户列表
	 */
	public List<Weather> getWeatherList() {
		String sql = "SELECT * FROM weather";
		List<Weather> customerList = DataBaseHelper.queryEntityList(Weather.class, sql);
		return customerList;
	}

	/*
	 * 获取客户列表
	 */
	public List<Customer> getCustomerList() {
		String sql = "SELECT * FROM CUSTOMER";
		List<Customer> customerList = DataBaseHelper.queryEntityList(Customer.class, sql);
		/*try {
			String sql = "SELECT * FROM CUSTOMER";
			conn = DataBaseHelper.getConnection();
			PreparedStatement stam = conn.prepareStatement(sql);
			ResultSet resultSet = stam.executeQuery();
			while (resultSet.next()) {
				Customer customer = new Customer();
				customer.setId(resultSet.getLong("id"));
				customer.setName(resultSet.getString("name"));
				customer.setContact(resultSet.getString("contact"));
				customer.setTelephone(resultSet.getString("telephone"));
				customer.setEmail(resultSet.getString("email"));
				customer.setRemark(resultSet.getString("remark"));
				customerList.add(customer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DataBaseHelper.closeConnction(conn);
		}*/
		return customerList;
	}
	
	/*
	 * 获取指定客户
	 */
	public Customer getCustomer(long id) {
		String sql = "SELECT * FROM CUSTOMER WHERE ID = " + id;
		Customer customer= DataBaseHelper.queryEntity(Customer.class, sql);
		return customer;
	}
	
	/*
	 * 创建客户
	 */
	public boolean createCustomer(Map<String, Object> customerMap) {
		return DataBaseHelper.insertEntity(Customer.class,customerMap);
	}
	
	/*
	 * 更新客户
	 */
	public boolean updateCustomer(long id, Map<String, Object> customerMap) {
		return DataBaseHelper.updateEntity(Customer.class, id, customerMap);
	}
	
	/*
	 * 删除客户
	 */
	public boolean delCustomer(long id) {
		return DataBaseHelper.deleteEntity(Customer.class,id);
	}
}
