package com.spring.cloud.demo.order.controller;

import com.spring.cloud.demo.order.feign.StoreFeignService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@RestController
public class OrderController {

    private final StoreFeignService storeFeignService;
    @GetMapping("/order/add")
    public String addOrder(){
        //使用nacos作为注册中心，来进行服务的发现
//        String msg = restTemplate.getForObject("http://store-nacos-demo/store/subtract", String.class);
        String msg = storeFeignService.subtractStore();
        return "下单成功！" + msg;
    }

}
