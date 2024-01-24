package com.spring.cloud.demo.order.feign;

import com.spring.cloud.demo.order.config.FeignConfig;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-nacos-demo", path = "/member", configuration = FeignConfig.class)
public interface MemberFeignService {

//    @GetMapping("/subtract/{id}")
    @RequestLine("GET /subtract/{id}")
//    String subtractMember(@PathVariable("id") String id);
    String subtractMember(@Param("id") String id);
}
