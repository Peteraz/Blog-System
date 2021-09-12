package com.example.blogsystemconsumer.hystrix;

import com.example.blogsystemconsumer.service.MailProviderService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
@Component
public class HystrixMailClientService implements FallbackFactory<MailProviderService> {
    @Override
    public MailProviderService create(Throwable throwable) {
        return new MailProviderService() {
            @Override
            public String SendMail(Object object){
                return "发送错误!";
            }
        };
    }
}
