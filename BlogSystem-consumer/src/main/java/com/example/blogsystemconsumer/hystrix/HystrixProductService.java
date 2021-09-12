package com.example.blogsystemconsumer.hystrix;

import com.example.blogsystemconsumer.service.ProductService;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

//@Component:把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>,也就是配置类
@Component
public class HystrixProductService implements FallbackFactory<ProductService> {
    @Override
    public ProductService create(Throwable throwable) {
        return new ProductService() {
            @Override
            public String getService(){
                return "连接超时，稍后重试。";
            }
        };
    }
}
