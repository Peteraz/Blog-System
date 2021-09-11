package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.hystrix.HystrixProductService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "product-server",fallback = HystrixProductService.class)
public interface ProductService {
    @RequestMapping(value="getService")
    String getService();
}
