package org.swift.chapter.controller;

import org.swift.chapter.model.Customer;
import org.swift.chapter.model.Weather;
import org.swift.chapter.service.CustomerService;
import org.swift.framework.annotation.Action;
import org.swift.framework.annotation.Controller;
import org.swift.framework.annotation.Inject;
import org.swift.framework.mvc.Param;

import java.util.List;

@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @Action(value = "get:/hello")
    public void hello(Param param) {
        List<Weather> list = customerService.getWeatherList();
        for (Weather weather : list) {
            System.out.println(weather);
        }
        System.out.println("hello " + list.size());
    }

}
