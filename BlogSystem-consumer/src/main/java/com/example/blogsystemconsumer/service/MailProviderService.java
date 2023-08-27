package com.example.blogsystemconsumer.service;

import com.example.blogsystemconsumer.resilience4j.BackendMailProviderService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
@Component
//name 为user-provider项目中application.yml配置文件中的application.name;
//path 为user-provider项目中application.yml配置文件中的context.path;
@FeignClient(name = "mail-provider-server", path = "/mail-provider", fallback = BackendMailProviderService.class)
public interface MailProviderService {

    /**
     * 发送邮件
     */
    @RequestMapping(value = "SendMail", method = RequestMethod.POST)
    String sendMail(@RequestParam("email") String email);
}
