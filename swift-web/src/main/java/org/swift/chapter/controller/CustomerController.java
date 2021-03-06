package org.swift.chapter.controller;

import org.swift.chapter.model.Customer;
import org.swift.chapter.model.Weather;
import org.swift.chapter.service.CustomerService;
import org.swift.framework.annotation.Action;
import org.swift.framework.annotation.Controller;
import org.swift.framework.annotation.Inject;
import org.swift.framework.helper.mq.RabbitTemplate;
import org.swift.framework.mvc.Data;
import org.swift.framework.mvc.Param;
import org.swift.framework.mvc.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @Action(value = "get:/hello")
    public Data hello(Param param) {
        List<Weather> list = customerService.getWeatherList();
        for (Weather weather : list) {
            System.out.println(weather);
        }
        System.out.println("hello " + list.size());
        return new Data(list);
    }

    @Action(value = "get:/helloView")
    public View helloView(Param param) {
//        List<Weather> list = customerService.getWeatherList();
//        for (Weather weather : list) {
//            System.out.println(weather);
//        }
//        System.out.println("hello " + list.size());
        return new View("/temp.html");
    }

    @Action(value = "get:/createHello")
    public void createHello(Param param) {
        Map<String, Object> customerMap = new HashMap<String, Object>();
        customerMap.put("w_id", null);
        customerMap.put("w_date", "5日（周三）");
        customerMap.put("w_detail", "多云");
        customerMap.put("w_temperature_low", "34");
        customerMap.put("w_temperature_high", "26℃");
        System.out.println("create hello " + customerService.createCustomer(customerMap));
    }

    public static void main(String[] args) {
        try {
            Customer customer = new Customer();
            customer.setName("nihaoya");
            //定义队列
            RabbitTemplate.declareQueue("nihao");
            //发送mq 信息
            RabbitTemplate.sendMq("", "nihao", customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭mq的连接和通道
            RabbitTemplate.closeMqChannelAndConnection();
        }
    }

}
