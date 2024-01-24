package com.spring.cloud.demo.order.controller;

import com.spring.cloud.demo.order.feign.MemberFeignService;
import com.spring.cloud.demo.order.feign.StoreFeignService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Member;

@AllArgsConstructor
@RestController
public class OrderController {

    private final StoreFeignService storeFeignService;
    private final MemberFeignService memberFeignService;
    @GetMapping("/order/add")
    public String addOrder(){
        //使用nacos作为注册中心，来进行服务的发现
//        String msg = restTemplate.getForObject("http://store-nacos-demo/store/subtract", String.class);
        String s = storeFeignService.subtractStore();
        String m = memberFeignService.subtractMember("5");
        return "下单成功！" + s + m;
    }

}
