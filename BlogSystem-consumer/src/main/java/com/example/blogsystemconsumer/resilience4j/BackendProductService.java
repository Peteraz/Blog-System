package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class BackendProductService implements ProductService {
    @Override
    public String getService() {
        return "连接超时,请稍后重试!";
    }
}
