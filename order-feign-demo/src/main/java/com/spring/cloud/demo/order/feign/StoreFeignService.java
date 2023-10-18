package com.spring.cloud.demo.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "store-nacos-demo", path = "/store")
public interface StoreFeignService {

    @GetMapping("/subtract")
    String subtractStore();

}
