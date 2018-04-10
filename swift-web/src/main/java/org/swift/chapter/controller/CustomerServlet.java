package org.swift.chapter.controller;

import org.swift.chapter.model.Customer;
import org.swift.chapter.service.CustomerService;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建客户对外访问
 * @author 旺旺
 *
 */
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

	private CustomerService customerService;

	@Override
	public void init() throws ServletException {
		customerService = new CustomerService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Customer> customerList = customerService.getCustomerList();
		req.setAttribute("customerList", customerList);
		System.out.println("count:"+customerList.size());
		req.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(req, resp);
	}

}
