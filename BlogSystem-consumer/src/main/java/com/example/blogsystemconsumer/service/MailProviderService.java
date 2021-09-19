package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.resilience4j.BackendMailProviderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
//@Componet注解最好加上，不加idea会显示有错误，但是不影响系统运行；
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "mail-provider-server",path="/mail-provider",fallback = BackendMailProviderService.class)
public interface MailProviderService {
    @RequestMapping(value="SendMail")//注册
    String SendMail(Object object);

}
