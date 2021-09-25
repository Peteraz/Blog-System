package com.example.blogsystemconsumer.resilience4j;

import com.example.blogsystemconsumer.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class BackendProductService implements ProductService {
    @Override
    public String getService() {
        return "连接超时,请稍后重试!";
    }
}
